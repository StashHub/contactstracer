package com.drgayno.contactstracer

import com.drgayno.contactstracer.util.LocaleUtils
import com.drgayno.contactstracer.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object AppConfig {

    const val CSV_VERSION = 4
    const val FIREBASE_REGION = "us-east1"

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    val collectionSeconds
        get() = firebaseRemoteConfig.getLong("collection_seconds")

    val waitingSeconds
        get() = firebaseRemoteConfig.getLong("waiting_seconds")

    val advertiseTxPower
        get() = overrideAdvertiseTxPower ?: firebaseRemoteConfig.getLong("advertise_tx_power").toInt()

    val advertiseMode
        get() = firebaseRemoteConfig.getLong("advertise_mode").toInt()

    val scanMode
        get() = firebaseRemoteConfig.getLong("scan_mode").toInt()

    val smsTimeoutSeconds
        get() = firebaseRemoteConfig.getLong("sms_timeout_seconds")

    val showVerifyLaterTimeoutSeconds
        get() = firebaseRemoteConfig.getLong("show_verify_later_timeout")

    val smsErrorTimeoutSeconds
        get() = firebaseRemoteConfig.getLong("sms_error_timeout_seconds")

    val advertiseRestartMinutes
        get() = firebaseRemoteConfig.getLong("advertise_restart_minutes")

    val criticalExpositionRssi
        get() = firebaseRemoteConfig.getLong("critical_exposition_rssi").toInt()

    val criticalExpositionMinutes
        get() = firebaseRemoteConfig.getLong("critical_exposition_minutes").toInt()

    val uploadWaitingMinutes
        get() = firebaseRemoteConfig.getLong("upload_waiting_minutes").toInt()

    val persistDataDays
        get() = firebaseRemoteConfig.getLong("persist_data_days").toInt()

    val shareAppDynamicLink
        get() = firebaseRemoteConfig.getString("share_app_dynamic_link")

    val proclamationLink
        get() = getLocalized("proclamation_link")

    val termsAndConditionsLink
        get() = getLocalized("terms_and_conditions_link")

    val showBatteryOptimizationTutorial
        get() = firebaseRemoteConfig.getBoolean("show_battery_opt_tutorial")

    val allowVerifyLater
        get() = firebaseRemoteConfig.getBoolean("allow_verify_later")

    val batteryOptimizationAsusMarkdown
        get() = getLocalized("battery_opt_asus_markdown")

    val batteryOptimizationLenovoMarkdown
        get() = getLocalized("battery_opt_lenovo_markdown")

    val batteryOptimizationSamsungMarkdown
        get() = getLocalized("battery_opt_samsung_markdown")

    val batteryOptimizationSonyMarkdown
        get() = getLocalized("battery_opt_sony_markdown")

    val batteryOptimizationXiaomiMarkdown
        get() = getLocalized("battery_opt_xiaomi_markdown")

    val batteryOptimizationHuaweiMarkdown
        get() = getLocalized("battery_opt_huawei_markdown")

    val myDataText
        get() = getLocalized("my_data_text")

    var overrideAdvertiseTxPower: Int? = null

    init {
        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else 3600)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults).addOnCompleteListener {
            print()
        }
    }

    fun fetchRemoteConfig() {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d("Config params updated: $updated")
                print()
            } else {
                Log.e("Config params update failed")
            }
        }
    }

    private fun print() {
        for (item in firebaseRemoteConfig.all) {
            Log.d("${item.key}: ${item.value.asString()}")
        }
    }

    /**
     * It tries current supported language first.
     * If it's not found, it tries English.
     */
    private fun getLocalized(key: String): String {
        val currentLanguage = LocaleUtils.getSupportedLanguage()
        return if (currentLanguage == "en") {
            firebaseRemoteConfig.getString(key)
        } else {
            val translatedValue = firebaseRemoteConfig.getString(key + "_" + currentLanguage)
            if (translatedValue.isEmpty()) {
                val englishTranslatedValue = firebaseRemoteConfig.getString(key + "_en")
                if (englishTranslatedValue.isEmpty()) {
                    firebaseRemoteConfig.getString(key)
                } else {
                    englishTranslatedValue
                }
            } else {
                translatedValue
            }
        }
    }
}