package com.drgayno.contactstracer.service

import com.drgayno.contactstracer.AppConfig.FIREBASE_REGION
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.util.Auth
import com.drgayno.contactstracer.util.Log
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject

class PushService : FirebaseMessagingService() {

    val prefs: SharedPrefsRepository by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Push received: ${message.data}")
    }

    override fun onNewToken(token: String) {
        Log.d("New push token: $token")
        if (Auth.isSignedIn()) {
            val data = hashMapOf(
                "buid" to prefs.getDeviceBuid(),
                "pushRegistrationToken" to token
            )
            Firebase.functions(FIREBASE_REGION).getHttpsCallable("changePushToken").call(data)
                .addOnSuccessListener {
                    Log.d("Push token updated")
                }.addOnFailureListener {
                    Log.e(it)
                }
        }
    }
}