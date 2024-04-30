package com.drgayno.contactstracer.ext

import androidx.navigation.NavController
import com.drgayno.contactstracer.nav.event.NavigationEvent

fun NavController.navigate(navEvent: NavigationEvent) {
    if (navEvent.resId != null) {
        navigate(navEvent.resId!!, navEvent.navArgs, navEvent.navOptions)
    } else if (navEvent.navDirections != null) {
        navigate(navEvent.navDirections!!, navEvent.navOptions)
    }
}