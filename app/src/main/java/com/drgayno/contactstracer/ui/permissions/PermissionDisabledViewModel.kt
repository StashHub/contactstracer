package com.drgayno.contactstracer.ui.permissions

import android.app.Application
import android.bluetooth.BluetoothManager
import androidx.annotation.StringRes
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ext.hasLocationPermission
import com.drgayno.contactstracer.ext.isBluetoothEnabled
import com.drgayno.contactstracer.ext.isLocationEnabled
import com.drgayno.contactstracer.nav.livedata.SafeMutableLiveData

class PermissionDisabledViewModel(
    private val bluetoothManager: BluetoothManager,
    private val app: Application
) : BasePermissionViewModel(bluetoothManager, app) {

    val state = SafeMutableLiveData(ScreenState.BT_DISABLED)

    fun initViewModel() {
        val bluetoothDisabled = !bluetoothManager.isBluetoothEnabled()
        val locationDisabled = !app.isLocationEnabled() || !app.hasLocationPermission()

        state.value = when {
            bluetoothDisabled && locationDisabled -> ScreenState.BT_LOCATION_DISABLED
            bluetoothDisabled -> ScreenState.BT_DISABLED
            locationDisabled -> ScreenState.LOCATION_DISABLED
            else -> ScreenState.ALL_ENABLED
        }

        if (state.value == ScreenState.ALL_ENABLED) {
            navigate(R.id.to_nav_dashboard_fragment)
        }
    }

    override fun goToNextScreen() {
        navigate(R.id.to_nav_dashboard_fragment)
    }

    @StringRes
    fun getButtonTitle(): Int {
        return when (state.value) {
            ScreenState.BT_DISABLED -> R.string.enable_bluetooth_button
            ScreenState.LOCATION_DISABLED -> R.string.enable_location_button
            ScreenState.BT_LOCATION_DISABLED -> R.string.enable_bt_location_button
            ScreenState.ALL_ENABLED -> R.string.enable_bluetooth_button
        }
    }


    enum class ScreenState {
        BT_DISABLED, LOCATION_DISABLED, BT_LOCATION_DISABLED, ALL_ENABLED
    }
}