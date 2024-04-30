package com.drgayno.contactstracer.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.drgayno.contactstracer.service.CovidService

class BluetoothStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (BluetoothAdapter.ACTION_STATE_CHANGED == intent?.action) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_OFF -> CovidService.update(context)
                BluetoothAdapter.STATE_ON -> CovidService.update(context)
            }

        }
    }


}