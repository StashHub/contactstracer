package com.drgayno.contactstracer.ui.permissions

import android.app.Application
import android.bluetooth.BluetoothManager
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.ext.hasLocationPermission
import com.drgayno.contactstracer.ext.isBluetoothEnabled
import com.drgayno.contactstracer.ext.isLocationEnabled
import com.drgayno.contactstracer.ui.permissions.onboarding.PermissionsOnboarding

abstract class BasePermissionViewModel(
    private val bluetoothManager: BluetoothManager,
    private val app: Application
) : BaseViewModel() {

    fun onBluetoothEnabled() {
        if (app.hasLocationPermission()) {
            onLocationPermissionGranted()
        } else {
            publish(PermissionsOnboarding(PermissionsOnboarding.Command.REQUEST_LOCATION_PERMISSION))
        }
    }

    fun onLocationPermissionGranted() {
        if (!app.isLocationEnabled()) {
            publish(PermissionsOnboarding(PermissionsOnboarding.Command.ENABLE_LOCATION))
        } else {
            goToNextScreen()
        }
    }

    fun onLocationPermissionDenied() {
        publish(PermissionsOnboarding(PermissionsOnboarding.Command.PERMISSION_REQUIRED))
    }

    fun enableBluetooth() {
        publish(PermissionsOnboarding(PermissionsOnboarding.Command.ENABLE_BT))
    }

    fun checkState() {
        if (bluetoothManager.isBluetoothEnabled()
            && app.isLocationEnabled()
            && app.hasLocationPermission()
        ) {
            goToNextScreen()
        }
    }

    abstract fun goToNextScreen()
}