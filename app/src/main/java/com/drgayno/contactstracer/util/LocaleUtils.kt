package com.drgayno.contactstracer.util

import com.drgayno.contactstracer.BuildConfig
import java.util.*

object LocaleUtils {

    fun getLocale(): String {
        return Locale.getDefault().toString()
    }

    fun getSupportedLanguage(): String {
        val currentLanguage = Locale.getDefault().language
        return if (BuildConfig.SUPPORTED_LANGUAGES.contains(currentLanguage)) {
            currentLanguage
        } else {
            "en"
        }
    }
}