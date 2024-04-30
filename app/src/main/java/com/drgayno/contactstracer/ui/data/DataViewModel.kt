package com.drgayno.contactstracer.ui.data

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.db.DatabaseRepository
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.util.DeviceInfo
import com.drgayno.contactstracer.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DataViewModel(
    private val dbRepo: DatabaseRepository,
    private val prefs: SharedPrefsRepository
) : BaseViewModel() {

    val allItems = ObservableArrayList<Any>()
    private val dateFormatter =
        SimpleDateFormat("EEE, d MMM yyyy 'at' h:mm:ss z", Locale.getDefault())
    private val timeFormatter = SimpleDateFormat("h:m", Locale.getDefault())

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        subscribeToDb()
    }

    private fun subscribeToDb() {
        subscribe(dbRepo.getAllDesc(), {
            Log.e(it)
        }) { newData ->
            allItems.clear()
            allItems.addAll(newData)
        }
    }

    fun formatDate(timestamp: Long): String {
        return dateFormatter.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return timeFormatter.format(Date(timestamp))
    }

    fun sendData() {
        val minutesSinceLastUpload =
            (System.currentTimeMillis() - prefs.getLastUploadTimestamp()) / 60000
        if (minutesSinceLastUpload < AppConfig.uploadWaitingMinutes) {
            publish(ExportEvent.PleaseWait(AppConfig.uploadWaitingMinutes - minutesSinceLastUpload.toInt()))
        } else {
            publish(ExportEvent.Confirmation)
        }
    }
}
