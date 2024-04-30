package com.drgayno.contactstracer.ui.permissions.onboarding

import android.app.Application
import android.bluetooth.BluetoothManager
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.ui.permissions.BasePermissionViewModel

class OnboardingPermissionViewModel(
    bluetoothManager: BluetoothManager,
    val app: Application
) : BasePermissionViewModel(bluetoothManager, app) {

    override fun goToNextScreen() {
        navigate(R.id.to_nav_login_fragment)
    }
}

class PermissionsOnboarding(val command: Command) : LiveEvent() {
    enum class Command {
        ENABLE_BT,
        REQUEST_LOCATION_PERMISSION,
        ENABLE_LOCATION,
        PERMISSION_REQUIRED
    }
}