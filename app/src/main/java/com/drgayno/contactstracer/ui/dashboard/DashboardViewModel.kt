package com.drgayno.contactstracer.ui.dashboard

import androidx.lifecycle.Observer
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.nav.livedata.SafeMutableLiveData
import com.drgayno.contactstracer.bt.BluetoothRepository
import com.drgayno.contactstracer.util.Auth
import com.drgayno.contactstracer.util.formatPhoneNumber

class DashboardViewModel(
    val bluetoothRepository: BluetoothRepository,
    private val prefs: SharedPrefsRepository
) : BaseViewModel() {

    val serviceRunning = SafeMutableLiveData(false)
    val phoneNumber = Auth.getPhoneNumber().formatPhoneNumber()
    val phoneNumberNotVerified = !Auth.isPhoneNumberVerified()

    private val serviceObserver = Observer<Boolean> { isRunning ->
        if (!isRunning && !prefs.getAppPaused()) {
            publish(DashboardCommandEvent(DashboardCommandEvent.Command.TURN_ON))
        } else {
            publish(DashboardCommandEvent(DashboardCommandEvent.Command.UPDATE_STATE))
        }
    }

    override fun onCleared() {
        serviceRunning.removeObserver(serviceObserver)
        super.onCleared()
    }

    fun init() {
        serviceRunning.observeForever(serviceObserver)
    }

    fun pause() {
        publish(DashboardCommandEvent(DashboardCommandEvent.Command.TURN_OFF))
    }

    fun start() {
        publish(DashboardCommandEvent(DashboardCommandEvent.Command.TURN_ON))
    }

    fun verifyPhoneNumber() {
        navigate(R.id.to_nav_login_fragment)
    }
}

class DashboardCommandEvent(val command: Command) : LiveEvent() {
    enum class Command {
        TURN_ON,
        TURN_OFF,
        UPDATE_STATE,
        PAUSE,
        RESUME,
    }
}
