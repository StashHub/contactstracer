package com.drgayno.contactstracer.ui

import com.drgayno.contactstracer.ui.base.BaseViewModel
import com.drgayno.contactstracer.nav.livedata.SafeMutableLiveData

class MainViewModel : BaseViewModel() {
    val serviceRunning = SafeMutableLiveData(false)
}