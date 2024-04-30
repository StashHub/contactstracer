package com.drgayno.contactstracer.nav.viewmodel

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.nav.event.LiveEventMap
import com.drgayno.contactstracer.nav.event.NavigationEvent
import kotlin.reflect.KClass

abstract class BaseArchViewModel : ViewModel(), LifecycleObserver {

    private val liveEventMap = LiveEventMap()

    fun <T : LiveEvent> subscribe(
        lifecycleOwner: LifecycleOwner,
        eventClass: KClass<T>,
        eventObserver: Observer<T>
    ) {
        liveEventMap.subscribe(lifecycleOwner, eventClass, eventObserver)
    }

    fun <T : LiveEvent> publish(event: T) {
        liveEventMap.publish(event)
    }

    protected fun navigate(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        publish(NavigationEvent(resId, args, navOptions))
    }

    protected fun navigate(directions: NavDirections, navOptions: NavOptions? = null) {
        publish(NavigationEvent(directions, navOptions))
    }
}
