package com.drgayno.contactstracer.ui.surveykit.backend.views.questions

import android.annotation.SuppressLint
import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.AnswerFormat
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.TextChoice
import com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts.MultipleChoicePart
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.QuestionView
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import com.drgayno.contactstracer.ui.surveykit.result.question_results.MultipleChoiceQuestionResult

@SuppressLint("ViewConstructor")
internal class MultipleChoiceQuestionView(
    context: Context,
    id: StepIdentifier,
    isOptional: Boolean,
    title: String?,
    text: String?,
    nextButtonText: String,
    private val answerFormat: AnswerFormat.MultipleChoiceAnswerFormat,
    private val preselected: List<TextChoice>?
) : QuestionView(context, id, isOptional, title, text, nextButtonText) {

    private lateinit var choicesContainer: MultipleChoicePart

    override fun createResults(): QuestionResult =
        MultipleChoiceQuestionResult(
            id = id,
            startDate = startDate,
            answer = choicesContainer.selected,
            stringIdentifier = choicesContainer.selected.joinToString(",") { it.value }
        )

    override fun isValidInput(): Boolean = isOptional || choicesContainer.isOneSelected()

    override fun setupViews() {
        super.setupViews()

        choicesContainer = content.add(MultipleChoicePart(context))
        choicesContainer.options = answerFormat.textChoices
        choicesContainer.onCheckedChangeListener = { _, _ -> footer.canContinue = isValidInput() }
        val preselectedOptions = preselected ?: emptyList()
        val selectedOptions =
            if (preselectedOptions.isNotEmpty()) preselectedOptions
            else answerFormat.defaultSelections
        choicesContainer.selected = selectedOptions
    }
}