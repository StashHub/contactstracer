package com.drgayno.contactstracer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.drgayno.contactstracer.service.CovidService
import com.drgayno.contactstracer.util.Log

class BatterSaverStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        Log.d("Battery saver state changed")
        if (action.equals(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)) {
            CovidService.update(context)
        }
    }
}