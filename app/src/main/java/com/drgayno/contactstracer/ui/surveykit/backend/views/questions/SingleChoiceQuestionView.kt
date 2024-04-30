package com.drgayno.contactstracer.ui.surveykit.backend.views.questions

import android.annotation.SuppressLint
import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.AnswerFormat
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.TextChoice
import com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts.SingleChoicePart
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.QuestionView
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import com.drgayno.contactstracer.ui.surveykit.result.question_results.SingleChoiceQuestionResult

@SuppressLint("ViewConstructor")
internal class SingleChoiceQuestionView(
    context: Context,
    id: StepIdentifier,
    isOptional: Boolean,
    title: String?,
    text: String?,
    nextButtonText: String,
    private val answerFormat: AnswerFormat.SingleChoiceAnswerFormat,
    private val preselected: TextChoice? = null
) : QuestionView(context, id, isOptional, title, text, nextButtonText) {

    private lateinit var choicesContainer: SingleChoicePart

    override fun createResults(): QuestionResult {
        val stringIdentifier = (answerFormat.textChoices
            .find { it.text == choicesContainer.selected?.text }
            ?.value
            ?: "")

        return SingleChoiceQuestionResult(
            id = id,
            startDate = startDate,
            answer = choicesContainer.selected,
            stringIdentifier = stringIdentifier
        )
    }

    override fun isValidInput(): Boolean = choicesContainer.isOneSelected()

    override fun setupViews() {
        super.setupViews()
        choicesContainer = content.add(SingleChoicePart(context))
        choicesContainer.options = answerFormat.textChoices
        footer.canBack = true
        choicesContainer.onCheckedChangeListener = { _, _ -> footer.canContinue = isValidInput() }
        choicesContainer.selected = preselected ?: answerFormat.defaultSelection
    }
}