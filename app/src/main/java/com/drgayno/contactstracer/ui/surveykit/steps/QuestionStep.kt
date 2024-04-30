package com.drgayno.contactstracer.ui.surveykit.steps

import android.content.Context
import com.drgayno.contactstracer.ui.surveykit.AnswerFormat
import com.drgayno.contactstracer.ui.surveykit.AnswerFormat.SingleChoiceAnswerFormat
import com.drgayno.contactstracer.ui.surveykit.AnswerFormat.MultipleChoiceAnswerFormat
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.backend.views.questions.MultipleChoiceQuestionView
import com.drgayno.contactstracer.ui.surveykit.backend.views.questions.SingleChoiceQuestionView
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.QuestionView
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.result.question_results.*

class QuestionStep(
    val title: String,
    val text: String,
    private val nextButton: String = "Next",
    val answerFormat: AnswerFormat,
    override var isOptional: Boolean = false,
    override val id: StepIdentifier = StepIdentifier()
) : Step {

    override fun createView(context: Context, stepResult: StepResult?): QuestionView =
        when (answerFormat) {
            is SingleChoiceAnswerFormat -> createSingleChoiceQuestion(context, stepResult)
            is MultipleChoiceAnswerFormat -> createMultipleChoiceQuestion(context, stepResult)
        }

    private fun createSingleChoiceQuestion(context: Context, stepResult: StepResult?) =
        SingleChoiceQuestionView(
            context = context,
            id = id,
            title = title,
            text = text,
            isOptional = isOptional,
            nextButtonText = nextButton,
            answerFormat = this.answerFormat as SingleChoiceAnswerFormat,
            preselected = stepResult.toSpecificResult<SingleChoiceQuestionResult>()?.answer
        )

    private fun createMultipleChoiceQuestion(context: Context, stepResult: StepResult?) =
        MultipleChoiceQuestionView(
            context = context,
            id = id,
            title = title,
            text = text,
            isOptional = isOptional,
            nextButtonText = nextButton,
            answerFormat = this.answerFormat as MultipleChoiceAnswerFormat,
            preselected = stepResult.toSpecificResult<MultipleChoiceQuestionResult>()?.answer
        )

    @Suppress("UNCHECKED_CAST")
    private fun <R : QuestionResult> StepResult?.toSpecificResult(): R? =
        (this?.results?.firstOrNull() as? R)

}