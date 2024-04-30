package com.drgayno.contactstracer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.drgayno.contactstracer.BuildConfig
import com.drgayno.contactstracer.service.CovidService

class ActionsReceiver : BroadcastReceiver() {
    companion object {
        private const val PREFIX = BuildConfig.APPLICATION_ID + ".TRACER"
        const val ACTION_PAUSE = "${PREFIX}_PAUSE"
        const val ACTION_RESUME = "${PREFIX}_RESUME"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            ACTION_PAUSE -> {
                context.startService(CovidService.stopService(context))
            }
            ACTION_RESUME -> {
                context.startService(CovidService.startService(context))
            }
        }
    }
}