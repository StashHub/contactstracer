package com.drgayno.contactstracer.ui.confirm

import androidx.lifecycle.viewModelScope
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.AppConfig.FIREBASE_REGION
import com.drgayno.contactstracer.db.DatabaseRepository
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.db.export.CsvExporter
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.ui.confirm.event.CompletedEvent
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.drgayno.contactstracer.ui.confirm.event.ErrorEvent
import com.drgayno.contactstracer.ui.confirm.event.LogoutEvent
import com.drgayno.contactstracer.util.Auth
import com.drgayno.contactstracer.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ConfirmationViewModel(
    private val database: DatabaseRepository,
    private val prefs: SharedPrefsRepository,
    private val exporter: CsvExporter
) : BaseViewModel() {

    companion object {
        const val UPLOAD_TIMEOUT_MILLIS = 30000L
    }

    private val functions = Firebase.functions(FIREBASE_REGION)
    private var exportDisposable: Disposable? = null
    private val storage = Firebase.storage

    override fun onCleared() {
        super.onCleared()
        exportDisposable?.dispose()
    }

    fun deleteAllData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = hashMapOf(
                        "buid" to prefs.getDeviceBuid()
                    )
                    functions.getHttpsCallable("deleteUploads").call(data).await()
                    database.clear()
                    publish(CompletedEvent)
                } catch (e: Exception) {
                    handleError(e)
                }
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    functions.getHttpsCallable("deleteUser").call().await()
                    database.clear()
                    prefs.clear()
                    Auth.signOut()
                    publish(CompletedEvent)
                } catch (e: Exception) {
                    handleError(e)
                }
            }
        }
    }

    fun sendData() {
        exportDisposable?.dispose()
        val buid = prefs.getDeviceBuid()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val data = hashMapOf(
                        "buid" to buid
                    )
                    val active = functions.getHttpsCallable("isBuidActive").call(data)
                        .await().data as? Boolean ?: false
                    if (!active) {
                        publish(LogoutEvent)
                    } else {
                        exportDisposable = exporter.export().subscribe({
                            uploadToStorage(it)
                        }, {
                            handleError(it)
                        })
                    }
                } catch (e: Exception) {
                    handleError(e)
                }
            }
        }
    }

    private fun uploadToStorage(csv: ByteArray) {
        val fuid = Auth.getFuid()
        val timestamp = System.currentTimeMillis()
        val buid = prefs.getDeviceBuid()
        storage.maxUploadRetryTimeMillis = UPLOAD_TIMEOUT_MILLIS;
        val ref = storage.reference.child("proximity/$fuid/$buid/$timestamp.csv")
        val metadata = storageMetadata {
            contentType = "text/csv"
            setCustomMetadata("version", AppConfig.CSV_VERSION.toString())
        }
        ref.putBytes(csv, metadata).addOnSuccessListener {
            prefs.saveLastUploadTimestamp(timestamp)
            publish(CompletedEvent)
        }.addOnFailureListener {
            handleError(it)
        }
    }

    private fun handleError(e: Throwable) {
        Log.e(e)
        if (e is FirebaseFunctionsException && e.code == FirebaseFunctionsException.Code.UNAUTHENTICATED) {
            publish(LogoutEvent)
        } else if (e is StorageException && e.errorCode == StorageException.ERROR_NOT_AUTHENTICATED) {
            publish(LogoutEvent)
        } else {
            publish(ErrorEvent(e))
        }
    }
}
