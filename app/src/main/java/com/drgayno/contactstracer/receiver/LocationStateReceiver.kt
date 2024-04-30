package com.drgayno.contactstracer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.drgayno.contactstracer.ext.batterySaverRestrictsLocation
import com.drgayno.contactstracer.service.CovidService
import com.drgayno.contactstracer.util.Log
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocationStateReceiver : BroadcastReceiver(), KoinComponent {

    private val powerManager by inject<PowerManager>()

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        Log.d("Location state changed")
        if (action.equals("android.location.PROVIDERS_CHANGED")
            && !powerManager.batterySaverRestrictsLocation()
        ) {
            CovidService.update(context)
        }
    }
}