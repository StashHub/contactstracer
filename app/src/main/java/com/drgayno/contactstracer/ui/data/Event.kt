package com.drgayno.contactstracer.ui.data

import com.drgayno.contactstracer.nav.event.LiveEvent

sealed class ExportEvent : LiveEvent() {
    data class PleaseWait(val minutes: Int) : ExportEvent()
    object Confirmation : ExportEvent()
}