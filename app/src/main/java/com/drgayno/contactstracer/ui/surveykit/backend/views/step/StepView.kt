package com.drgayno.contactstracer.ui.surveykit.backend.views.step

import android.content.Context
import android.widget.FrameLayout
import com.drgayno.contactstracer.ui.surveykit.FinishReason
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult

abstract class StepView(
    context: Context,
    override val id: StepIdentifier,
    override val isOptional: Boolean
) : FrameLayout(context), ViewActions {

    protected var onNextListener: (QuestionResult) -> Unit = {}
    override fun onNext(block: (QuestionResult) -> Unit) {
        onNextListener = block
    }

    protected var onBackListener: (QuestionResult) -> Unit = {}
    override fun onBack(block: (QuestionResult) -> Unit) {
        onBackListener = block
    }

    protected var onCloseListener: (QuestionResult, FinishReason) -> Unit = { _, _ -> }
    override fun onClose(block: (QuestionResult, FinishReason) -> Unit) {
        onCloseListener = block
    }

    protected var onSkipListener: () -> Unit = {}
    override fun onSkip(block: () -> Unit) {
        onSkipListener = block
    }

    override fun back() = onBackListener(createResults())

    abstract fun setupViews()
    open fun onViewCreated() = Unit
}