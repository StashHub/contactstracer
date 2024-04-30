package com.drgayno.contactstracer.ui.surveykit.steps

import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.StepView
import com.drgayno.contactstracer.ui.surveykit.result.StepResult

interface Step {
    val isOptional: Boolean
    val id: StepIdentifier
    fun createView(context: Context, stepResult: StepResult?): StepView
}