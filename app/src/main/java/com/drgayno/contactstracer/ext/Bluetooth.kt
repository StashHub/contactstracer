package com.drgayno.contactstracer.ext

import android.bluetooth.BluetoothManager

fun BluetoothManager.isBluetoothEnabled(): Boolean {
    return adapter?.isEnabled ?: false
}