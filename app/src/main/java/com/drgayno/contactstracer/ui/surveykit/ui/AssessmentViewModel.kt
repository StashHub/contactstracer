package com.drgayno.contactstracer.ui.surveykit.ui

import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.ui.base.BaseViewModel

class AssessmentViewModel : BaseViewModel() {
    fun getStarted() {
        publish(SurveyEvent(SurveyEvent.Command.START))
    }
}

class SurveyEvent(
    val command: Command
) : LiveEvent() {
    enum class Command {
        START
    }
}
