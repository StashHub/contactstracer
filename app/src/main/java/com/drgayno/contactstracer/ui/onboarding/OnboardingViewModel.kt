package com.drgayno.contactstracer.ui.onboarding

import android.app.Application
import android.bluetooth.BluetoothManager
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.ext.hasLocationPermission
import com.drgayno.contactstracer.ext.isBluetoothEnabled
import com.drgayno.contactstracer.ext.isLocationEnabled
import com.drgayno.contactstracer.nav.event.LiveEvent

class OnboardingViewModel(
    private val app: Application,
    private val bluetoothManager: BluetoothManager
) : BaseViewModel() {

    fun nextStep() {
        if (hasPermissions()) {
            publish(OnboardingEvent(OnboardingEvent.Command.VERIFY))
        } else {
            publish(OnboardingEvent(OnboardingEvent.Command.BLUETOOTH))
        }
    }

    private fun hasPermissions() =
        bluetoothManager.isBluetoothEnabled() && app.isLocationEnabled() && app.hasLocationPermission()

    fun help() {
        publish(OnboardingEvent(OnboardingEvent.Command.HELP))
    }

    fun getProclamationUrl(): String {
        return AppConfig.proclamationLink
    }

    fun getStarted() {
        publish(OnboardingEvent(OnboardingEvent.Command.PREVENT))
    }
}

class OnboardingEvent(
    val command: Command
) : LiveEvent() {
    enum class Command {
        VERIFY,
        BLUETOOTH,
        PREVENT,
        HELP
    }
}
