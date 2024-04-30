package com.drgayno.contactstracer.ui.login

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.AppConfig.FIREBASE_REGION
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.functions.ktx.functions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.util.*
import java.text.SimpleDateFormat
import java.util.*

class LoginViewModel(
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val deviceInfo: DeviceInfo,
    private val context: Context
) : BaseViewModel() {

    private val mutableState = MutableLiveData<LoginState>(EnterPhoneNumber(false))
    val state = mutableState as LiveData<LoginState>
    val verifyLaterShown = MutableLiveData(false)
    val verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            val smsCode = credential.smsCode
            if (smsCode == null) {
                signInWithPhoneAuthCredential(credential)
            } else {
                autoVerifiedCredential = credential
                mutableState.postValue(CodeReadAutomatically(smsCode))
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.d("onVerificationFailed")
            handleError(e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            this@LoginViewModel.verificationId = verificationId
            resendToken = token
            mutableState.postValue(EnterCode(false, phoneNumber))
            smsCountDownTimer.cancel()
            showVerifyLaterTimer.cancel()
            smsCountDownTimer.start()
            if (AppConfig.allowVerifyLater && !isConnectingAnonymousAccount()) {
                showVerifyLaterTimer.start()
            }
        }

        override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
            this@LoginViewModel.verificationId = verificationId
        }
    }

    private val registrationCompleteListener = OnCompleteListener<AuthResult> { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            registerDevice(true)
        } else {
            // Sign in failed, display a message and update the UI
            task.exception?.let { handleError(it) }
        }
    }

    private var autoVerifiedCredential: PhoneAuthCredential? = null
    private lateinit var verificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val functions = Firebase.functions(FIREBASE_REGION)
    private lateinit var phoneNumber: String

    private var smsCountDownTimer: CountDownTimer =
        object : CountDownTimer(AppConfig.smsErrorTimeoutSeconds * 1000, 1000) {
            override fun onFinish() {
                mutableState.postValue(LoginError(R.string.login_session_expired.toText(), true))
            }

            override fun onTick(millisUntilFinished: Long) {
                val df = SimpleDateFormat("mm:ss", Locale.getDefault())
                remainingTime.postValue(df.format(millisUntilFinished))
            }

        }

    private var showVerifyLaterTimer: CountDownTimer =
        object : CountDownTimer(AppConfig.showVerifyLaterTimeoutSeconds * 1000, 1000) {
            override fun onFinish() {
                verifyLaterShown.postValue(true)
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }

    val remainingTime = MutableLiveData("")

    init {
        auth.setLanguageCode(LocaleUtils.getSupportedLanguage())
        if (Auth.isSignedIn() && Auth.isPhoneNumberVerified()) {
            mutableState.postValue(SignedIn)
        }
    }

    fun phoneNumberEntered(phoneNumber: String) {
        // Ignore all spaces
        this.phoneNumber = phoneNumber
        if (this.phoneNumber.isEmpty() || !this.phoneNumber.isValidPhone(context = context)) {
            mutableState.postValue(EnterPhoneNumber(true))
        } else {
            mutableState.postValue(SigningProgress)
            publish(StartVerificationEvent)
        }
    }

    fun codeEntered(code: String) {
        if (code.trim().length != 6) {
            mutableState.postValue(EnterCode(true, phoneNumber))
        } else {
            mutableState.postValue(SigningProgress)
            val credential =
                autoVerifiedCredential ?: PhoneAuthProvider.getCredential(verificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }

    override fun onCleared() {
        smsCountDownTimer.cancel()
        showVerifyLaterTimer.cancel()
        super.onCleared()
    }

    fun backPressed() {
        smsCountDownTimer.cancel()
        showVerifyLaterTimer.cancel()
        mutableState.postValue(EnterPhoneNumber(false))
        verifyLaterShown.postValue(false)
    }

    fun verifyLater() {
        mutableState.postValue(SigningProgress)
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                registerDevice(false)
            } else {
                task.exception?.let { handleError(it) }
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        if (isConnectingAnonymousAccount()) {
            // Link anonymous user
            FirebaseAuth.getInstance().currentUser?.linkWithCredential(credential)
                ?.addOnCompleteListener(registrationCompleteListener)
        } else {
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(registrationCompleteListener)
        }
    }

    private fun handleError(exception: Exception) {
        if (exception is FirebaseAuthInvalidCredentialsException) {
            Log.d("Error code: ${exception.errorCode}")
        }
        verifyLaterShown.postValue(false)
        when (exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                exception.handle()
            }
            is FirebaseTooManyRequestsException -> {
                mutableState.postValue(
                    LoginError(
                        R.string.login_too_many_attempts_error.toText(),
                        true
                    )
                )
            }
            is FirebaseNetworkException -> {
                mutableState.postValue(LoginError(R.string.login_network_error.toText(), false))
            }
            is FirebaseAuthUserCollisionException -> {
                mutableState.postValue(
                    LoginError(
                        R.string.login_number_already_in_use_error.toText(phoneNumber.formatPhoneNumber()),
                        true
                    )
                )
            }
            else -> {
                Log.e(exception)
                mutableState.postValue(LoginError(exception.toString().toText(), true))
            }
        }
    }

    private fun FirebaseAuthInvalidCredentialsException.handle() {
        when (errorCode) {
            "ERROR_INVALID_PHONE_NUMBER" -> {
                mutableState.postValue(EnterPhoneNumber(true))
            }
            "ERROR_INVALID_VERIFICATION_CODE" -> {
                mutableState.postValue(EnterCode(true, phoneNumber))
            }
            "ERROR_TOO_MANY_REQUESTS" -> {
                mutableState.postValue(
                    LoginError(
                        R.string.login_too_many_attempts_error.toText(),
                        true
                    )
                )
            }
            "ERROR_SESSION_EXPIRED" -> {
                mutableState.postValue(LoginError(R.string.login_session_expired.toText(), true))
            }
            else -> {
                Log.e(ErrorCodeException(errorCode, this))
                mutableState.postValue(EnterCode(true, phoneNumber))
            }
        }
    }

    private fun registerDevice(phoneNumberVerified: Boolean) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                val pushToken = if (!task.isSuccessful) {
                    task.exception?.let { Log.e(it) }
                    "error"
                } else {
                    task.result?.token ?: "error"
                }

                val data = hashMapOf(
                    "platform" to "android",
                    "platformVersion" to deviceInfo.getAndroidVersion(),
                    "manufacturer" to deviceInfo.getManufacturer(),
                    "model" to deviceInfo.getDeviceName(),
                    "locale" to LocaleUtils.getLocale(),
                    "pushRegistrationToken" to pushToken
                )
                if (!phoneNumberVerified) {
                    data["unverifiedPhoneNumber"] = phoneNumber
                }
                functions.getHttpsCallable("registerBuid").call(data).addOnSuccessListener {
                    val response =
                        Gson().fromJson(it.data.toString(), RegistrationResponse::class.java)
                    sharedPrefsRepository.putDeviceBuid(response.buid)
                    sharedPrefsRepository.putDeviceTuids(response.tuids)
                    sharedPrefsRepository.saveAuthPhoneNumber(phoneNumber)
                    mutableState.postValue(SignedIn)
                }.addOnFailureListener {
                    handleError(it)
                }
            }
    }

    private fun isConnectingAnonymousAccount(): Boolean {
        return Auth.isSignedIn() && !Auth.isPhoneNumberVerified()
    }

    data class RegistrationResponse(val buid: String, val tuids: List<String>)

    class ErrorCodeException(errorCode: String, exception: Exception) :
        Throwable(exception.message + " Error code: $errorCode", exception)
}
