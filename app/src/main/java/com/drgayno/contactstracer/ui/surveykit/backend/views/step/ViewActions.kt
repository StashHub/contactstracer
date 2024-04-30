package com.drgayno.contactstracer.ui.surveykit.backend.views.step

import com.drgayno.contactstracer.ui.surveykit.FinishReason
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult

interface ViewActions : Skipable,
    Identifiable {
    fun onNext(block: (QuestionResult) -> Unit)
    fun onBack(block: (QuestionResult) -> Unit)
    fun onClose(block: (QuestionResult, FinishReason) -> Unit)

    fun createResults(): QuestionResult
    fun isValidInput(): Boolean

    fun back()
}