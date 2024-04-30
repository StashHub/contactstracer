package com.drgayno.contactstracer.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import androidx.browser.customtabs.CustomTabsService
import com.drgayno.contactstracer.AppConfig

class CustomTabHelper(
    private val context: Context
) {

    private var sPackageNameToUse: String? = null
    private val stablePackage = "com.android.chrome"
    private val betaPackage = "com.chrome.beta"
    private val devPackage = "com.chrome.dev"
    private val localPackage = "com.google.android.apps.chrome"

    val chromePackageName by lazy {
        return@lazy getPackageNameToUse(context, AppConfig.proclamationLink)
    }

    private fun getPackageNameToUse(context: Context, url: String): String? {

        sPackageNameToUse?.let {
            return it
        }

        val pm = context.packageManager

        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null

        defaultViewHandlerInfo?.let {
            defaultViewHandlerPackageName = it.activityInfo.packageName
        }

        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)

            pm.resolveService(serviceIntent, 0)?.let {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        when {
            packagesSupportingCustomTabs.isEmpty() -> sPackageNameToUse = null
            packagesSupportingCustomTabs.size == 1 -> sPackageNameToUse =
                packagesSupportingCustomTabs.get(0)
            !TextUtils.isEmpty(defaultViewHandlerPackageName)
                    && !hasSpecializedHandlerIntents(context, activityIntent)
                    && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName) ->
                sPackageNameToUse = defaultViewHandlerPackageName
            packagesSupportingCustomTabs.contains(stablePackage) -> sPackageNameToUse =
                stablePackage
            packagesSupportingCustomTabs.contains(betaPackage) -> sPackageNameToUse = betaPackage
            packagesSupportingCustomTabs.contains(devPackage) -> sPackageNameToUse = devPackage
            packagesSupportingCustomTabs.contains(localPackage) -> sPackageNameToUse =
                localPackage
        }
        return sPackageNameToUse
    }

    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER
            )
            if (handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
        }
        return false
    }
}
