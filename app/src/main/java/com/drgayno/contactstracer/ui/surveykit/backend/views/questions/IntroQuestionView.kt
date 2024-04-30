package com.drgayno.contactstracer.ui.surveykit.backend.views.questions

import android.annotation.SuppressLint
import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.QuestionView
import com.drgayno.contactstracer.ui.surveykit.result.question_results.IntroQuestionResult

@SuppressLint("ViewConstructor")
class IntroQuestionView(
    context: Context,
    id: StepIdentifier,
    isOptional: Boolean = false,
    title: String?,
    text: String?,
    startButtonText: String
) : QuestionView(context, id, isOptional, title, text, startButtonText) {

    override fun createResults() = IntroQuestionResult(id, startDate)

    override fun isValidInput() = true

    override fun setupViews() {
        super.setupViews()
        footer.canBack = false
    }
}