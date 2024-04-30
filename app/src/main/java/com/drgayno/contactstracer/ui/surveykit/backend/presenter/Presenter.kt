package com.drgayno.contactstracer.ui.surveykit.backend.presenter

import android.content.Context
import android.widget.FrameLayout
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.steps.Step

interface Presenter {
    val context: Context
    val viewContainer: FrameLayout

    suspend operator fun invoke(
        transition: Transition,
        step: Step,
        stepResult: StepResult?
    ): NextAction

    fun triggerBackOnCurrentView()

    enum class Transition {
        None, SlideFromRight, SlideFromLeft;
    }
}