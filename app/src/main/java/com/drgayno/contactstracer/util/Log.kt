package com.drgayno.contactstracer.util

import android.util.Log
import com.crashlytics.android.BuildConfig
import com.crashlytics.android.Crashlytics

object Log {

    fun d(text: String) {
        val logStrings = createLogStrings(text)
        if (BuildConfig.DEBUG) {
            Log.d(logStrings[0], logStrings[1])
        }
    }

    fun i(text: String) {
        val logStrings = createLogStrings(text)
        Crashlytics.log(Log.INFO, logStrings[0], logStrings[1])
    }

    fun w(text: String) {
        val logStrings = createLogStrings(text)
        if (BuildConfig.DEBUG) {
            Log.w(logStrings[0], logStrings[1])
        }
        Crashlytics.log(Log.WARN, logStrings[0], logStrings[1])
    }

    fun e(text: String) {
        val logStrings = createLogStrings(text)
        Crashlytics.log(Log.ERROR, logStrings[0], logStrings[1])
    }

    fun e(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("Log", throwable.message, throwable)
        }
        Crashlytics.logException(throwable)
    }

    private fun createLogStrings(text: String): Array<String> {
        val ste = Thread.currentThread().stackTrace
        val line = "(" + (ste[4].fileName + ":" + ste[4].lineNumber + ")")
        return arrayOf(line, text)
    }
}
