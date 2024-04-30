package com.drgayno.contactstracer.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.service.CovidService

class ShortcutsManager(private val context: Context) {

    companion object {
        object ShortcutsActions {
            private const val SCHEME = "contactstracer"

            val URL_PAUSE: Uri = Uri.parse("${SCHEME}://service/pause")
            val URL_RESUME: Uri = Uri.parse("${SCHEME}://service/resume")
        }
    }

    fun handleShortcut(intent: Intent) {
        if (intent.action == Intent.ACTION_RUN) {
            when (intent.data) {
                ShortcutsActions.URL_PAUSE -> {
                    context.startService(CovidService.stopService(context))
                }
                ShortcutsActions.URL_RESUME -> {
                    context.startService(CovidService.startService(context))
                }
            }
        }
    }

    fun updateShortcuts(isRunning: Boolean) {
        ShortcutManagerCompat.removeAllDynamicShortcuts(context)

        val shortcut = if (isRunning) {
            ShortcutInfoCompat.Builder(context, "contactstracer-service")
                .setShortLabel(context.getString(R.string.pause_app))
                .setLongLabel(context.getString(R.string.pause_app))
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_pan_tool_24px))
                .setIntent(
                    Intent(
                        Intent.ACTION_RUN,
                        ShortcutsActions.URL_PAUSE
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                .build()
        } else {
            ShortcutInfoCompat.Builder(context, "contactstracer-service")
                .setShortLabel(context.getString(R.string.start_app))
                .setLongLabel(context.getString(R.string.start_app))
                .setIcon(IconCompat.createWithResource(context, R.drawable.ic_check_circle_24px))
                .setIntent(
                    Intent(
                        Intent.ACTION_RUN,
                        ShortcutsActions.URL_RESUME
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                .build()
        }

        ShortcutManagerCompat.addDynamicShortcuts(context, listOf(shortcut))
    }
}