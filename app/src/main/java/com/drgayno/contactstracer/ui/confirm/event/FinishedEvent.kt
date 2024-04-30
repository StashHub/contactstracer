package com.drgayno.contactstracer.ui.confirm.event

import com.drgayno.contactstracer.nav.event.LiveEvent

object CompletedEvent : LiveEvent()
data class ErrorEvent(val exception: Throwable) : LiveEvent()
object LogoutEvent : LiveEvent()
