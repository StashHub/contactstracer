package com.drgayno.contactstracer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.service.CovidService
import com.drgayno.contactstracer.util.Log

class AutoRestartReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRAKEY_START = "AUTO_RESTART_START"
        const val EXTRAKEY_CANCEL = "AUTO_RESTART_CANCEL"

        const val REQUEST_CODE = 123
        const val URI_PATH = "restart"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val uri = Uri.parse("${context.getString(R.string.uri_scheme)}://$URI_PATH")

        if (intent.action == Intent.ACTION_RUN && intent.data == uri) {
            // do this stuff only when service is running!
            //
            // if it's not running, it should actually not eve get here, however, it's better to
            // check it to avoid possible problems like:
            // `Unable to start receiver  java.lang.IllegalStateException: Not allowed to start service`
            if (CovidService.isRunning(context)) {
                Log.d("Auto-restart of service - stopping")
                context.startService(
                    CovidService.stopService(context)
                        .putExtra(EXTRAKEY_CANCEL, false)
                )

                Handler().postDelayed({
                    Log.d("Auto-restart of service - starting")
                    context.startService(
                        CovidService.startService(context)
                            .putExtra(EXTRAKEY_START, false)
                    )
                }, 2000);
            }
        }
    }
}