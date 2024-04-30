package com.drgayno.contactstracer.ui.surveykit.backend.views.questions

import android.annotation.SuppressLint
import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.FinishReason
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts.QuestionAnimation
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.QuestionView
import com.drgayno.contactstracer.ui.surveykit.result.question_results.FinishQuestionResult
import com.drgayno.contactstracer.ui.surveykit.steps.CompletionStep

@SuppressLint("ViewConstructor")
internal class FinishQuestionView(
    context: Context,
    id: StepIdentifier = StepIdentifier(),
    title: String?,
    text: String?,
    finishButtonText: String,
    private val lottieAnimation: CompletionStep.LottieAnimation?,
    private val repeatCount: Int?
) : QuestionView(context, id, false, title, text, finishButtonText) {

    override fun createResults() =
        FinishQuestionResult(id, startDate)

    override fun isValidInput() = true

    override fun setupViews() {
        super.setupViews()
        content.add(
            QuestionAnimation(
                context = context,
                animation = lottieAnimation,
                repeatCount = repeatCount
            )
        )

        footer.onContinue = { onCloseListener(createResults(), FinishReason.Completed) }
    }
}