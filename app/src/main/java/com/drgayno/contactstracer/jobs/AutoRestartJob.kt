package com.drgayno.contactstracer.jobs

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.receiver.AutoRestartReceiver
import com.drgayno.contactstracer.util.Log
import org.threeten.bp.ZonedDateTime

class AutoRestartJob {

    private var pendingIntent: PendingIntent? = null
    private var alarmManager: AlarmManager? = null

    fun setUp(context: Context, alarmManager: AlarmManager) {
        val uri =
            Uri.parse("${context.getString(R.string.uri_scheme)}://${AutoRestartReceiver.URI_PATH}")

        val intent = Intent(context, AutoRestartReceiver::class.java)
            .setAction(Intent.ACTION_RUN)
            .setData(uri)

        pendingIntent = PendingIntent.getBroadcast(
            context,
            AutoRestartReceiver.REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        this.alarmManager = alarmManager

        val now = ZonedDateTime.now()
        // set it to the closest 2:00 (even when if it's 0:00 - 2:00 now)
        val first = (if (now.hour < 2) now else now.plusDays(1)).withHour(2).withMinute(0)

        Log.d("Planning auto-restart with interval 24h, first: $first")

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            first.toInstant().toEpochMilli(),
            INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancel() {
        Log.d("Cancelling auto-restart")
        pendingIntent?.let { alarmManager?.cancel(it) }
    }
}