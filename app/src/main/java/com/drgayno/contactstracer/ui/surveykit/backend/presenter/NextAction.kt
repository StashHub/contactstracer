package com.drgayno.contactstracer.ui.surveykit.backend.presenter

import com.drgayno.contactstracer.ui.surveykit.FinishReason
import com.drgayno.contactstracer.ui.surveykit.result.StepResult

sealed class NextAction {
    data class Next(val result: StepResult) : NextAction()
    data class Back(val result: StepResult) : NextAction()
    object Skip : NextAction()
    data class Close(val result: StepResult, val finishReason: FinishReason) : NextAction()
}