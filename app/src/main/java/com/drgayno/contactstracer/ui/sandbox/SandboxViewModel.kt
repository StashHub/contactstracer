package com.drgayno.contactstracer.ui.sandbox

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.nav.livedata.SafeMutableLiveData
import com.drgayno.contactstracer.bt.BluetoothRepository

class SandboxViewModel(
    val bluetoothRepository: BluetoothRepository,
    prefs: SharedPrefsRepository
) : BaseViewModel() {

    val buid = prefs.getDeviceBuid()
    val tuid = SafeMutableLiveData(prefs.getCurrentTuid() ?: "")
    val devices = bluetoothRepository.scanResultsList
    val serviceRunning = SafeMutableLiveData(false)
    val power = SafeMutableLiveData(0)
    val advertisingSupportText = MutableLiveData<String>().apply {
        value = if (bluetoothRepository.supportsAdvertising()) {
            "Supports broadcasting"
        } else {
            "Does not support broadcasting"
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        power.observeForever { value ->
            if (value == 0) {
                // 0 means remote config
                AppConfig.overrideAdvertiseTxPower = null
            } else {
                // save overriding power setting (value-1)
                AppConfig.overrideAdvertiseTxPower = value - 1
            }
        }
    }

    fun start() {
        // publish(DashboardCommandEvent(DashboardCommandEvent.Command.TURN_ON))
    }

    fun confirmStart() {
        serviceRunning.value = true
    }

    fun stop() {
        serviceRunning.value = false
        //publish(DashboardCommandEvent(DashboardCommandEvent.Command.TURN_OFF))
    }


    fun openDbExplorer() {
        //navigate(R.id.action_nav_sandbox_to_nav_my_data)
    }
}