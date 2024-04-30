package com.drgayno.contactstracer.ui.surveykit.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentSurveyBinding
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.ui.surveykit.*
import com.drgayno.contactstracer.ui.surveykit.backend.views.step.StepView
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.result.TaskResult
import com.drgayno.contactstracer.ui.surveykit.result.question_results.SingleChoiceQuestionResult
import com.drgayno.contactstracer.ui.surveykit.steps.CompletionStep
import com.drgayno.contactstracer.ui.surveykit.steps.InstructionStep
import com.drgayno.contactstracer.ui.surveykit.steps.QuestionStep
import com.drgayno.contactstracer.ui.surveykit.steps.Step
import com.drgayno.contactstracer.ui.surveykit.survey.SurveyView
import kotlinx.android.parcel.Parcelize
import java.util.*

class SurveyFragment :
    BaseFragment<FragmentSurveyBinding, AssessmentViewModel>(
        R.layout.fragment_survey, AssessmentViewModel::class
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true, IconType.CLOSE)
        setupSurvey(binding.surveyView)
    }

    private fun setupSurvey(surveyView: SurveyView) {
        val steps = listOf(
            IntroScreening(),
            EmergencyStep(),
            QuestionStep( // 3
                title = this.resources.getString(R.string.age_question),
                text = "",
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.age_question_17_under_answer)),
                        TextChoice(this.resources.getString(R.string.age_question_18_64_answer)),
                        TextChoice(this.resources.getString(R.string.age_question_65_up_answer))
                    )
                )
            ),
            Under18Step(),
            QuestionStep( // 4
                title = this.resources.getString(R.string.symptoms_question),
                text = this.resources.getString(R.string.select_all_that_apply),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.symptom_fever_chills)),
                        TextChoice(this.resources.getString(R.string.symptom_diff_breathing)),
                        TextChoice(this.resources.getString(R.string.symptom_coughing)),
                        TextChoice(this.resources.getString(R.string.symptom_appetite)),
                        TextChoice(this.resources.getString(R.string.symptom_sore_throat)),
                        TextChoice(this.resources.getString(R.string.symptom_body_aches)),
                        TextChoice(this.resources.getString(R.string.symptom_vomiting)),
                        TextChoice(this.resources.getString(R.string.none_of_the_above))
                    )
                )
            ),
            QuestionStep( // 5
                title = this.resources.getString(R.string.healthcare_question),
                text = this.resources.getString(R.string.healthcare_question_caption),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.yes)),
                        TextChoice(this.resources.getString(R.string.no))
                    )
                )
            ),
            QuestionStep( // 6
                title = this.resources.getString(R.string.last_2wks_question),
                text = this.resources.getString(R.string.select_all_that_apply),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.wks_close_contact)),
                        TextChoice(this.resources.getString(R.string.wks_travel)),
                        TextChoice(this.resources.getString(R.string.wks_visit)),
                        TextChoice(this.resources.getString(R.string.none_of_the_above))
                    )
                )
            ),
            ResultProtectYourself(), // 7
            ResultStayHome(), // 8
            ContactProvider(), // 9
            // TODO Navigation rule base on previous response
            QuestionStep( // 10
                title = this.resources.getString(R.string.last_2wks_question),
                text = this.resources.getString(R.string.select_all_that_apply),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.wks_close_contact)),
                        TextChoice(this.resources.getString(R.string.wks_travel)),
                        TextChoice(this.resources.getString(R.string.wks_visit)),
                        TextChoice(this.resources.getString(R.string.none_of_the_above))
                    )
                )
            ),
            QuestionStep( // 11
                title = this.resources.getString(R.string.last_2wks_question),
                text = this.resources.getString(R.string.select_all_that_apply),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.wks_close_contact)),
                        TextChoice(this.resources.getString(R.string.wks_travel)),
                        TextChoice(this.resources.getString(R.string.wks_visit)),
                        TextChoice(this.resources.getString(R.string.none_of_the_above))
                    )
                )
            ),
            QuestionStep( // 12
                title = this.resources.getString(R.string.care_facility_question),
                text = this.resources.getString(R.string.care_facility_caption),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.yes)),
                        TextChoice(this.resources.getString(R.string.no))
                    )
                )
            ),
            QuestionStep( // 13
                title = this.resources.getString(R.string.healthcare_question),
                text = this.resources.getString(R.string.healthcare_question_caption),
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.yes)),
                        TextChoice(this.resources.getString(R.string.no))
                    )
                )
            ),
            QuestionStep( // 14
                title = this.resources.getString(R.string.conditions_question),
                text = this.resources.getString(R.string.select_all_that_apply),
                answerFormat = AnswerFormat.MultipleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice(this.resources.getString(R.string.conditions_lung_disease)),
                        TextChoice(this.resources.getString(R.string.conditions_asthma)),
                        TextChoice(this.resources.getString(R.string.conditions_heart)),
                        TextChoice(this.resources.getString(R.string.conditions_diabetes)),
                        TextChoice(this.resources.getString(R.string.conditions_cough)),
                        TextChoice(this.resources.getString(R.string.conditions_immune_system)),
                        TextChoice(this.resources.getString(R.string.conditions_dialysis)),
                        TextChoice(this.resources.getString(R.string.conditions_liver_disease)),
                        TextChoice(this.resources.getString(R.string.conditions_pregnancy)),
                        TextChoice(this.resources.getString(R.string.conditions_obesity)),
                        TextChoice(this.resources.getString(R.string.none_of_the_above))
                    )
                )
            )
        )

        val task = NavigableOrderedTask(steps = steps)

        // Age condition navigation rule
        task.setNavigationRule(
            steps[2].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.age_question_17_under_answer) -> steps[3].id
                        else -> steps[4].id
                    }
                }
            )
        )

        // Symptoms condition navigation rule
        task.setNavigationRule(
            steps[4].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.none_of_the_above) -> steps[5].id
                        else -> steps[11].id
                    }
                }
            )
        )

        task.setNavigationRule(
            steps[5].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.yes) -> steps[10].id
                        else -> steps[6].id
                    }
                }
            )
        )

        // Last 2 weeks condition navigation rule
        task.setNavigationRule(
            steps[6].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.none_of_the_above) -> steps[7].id
                        else -> steps[8].id
                    }
                }
            )
        )

        task.setNavigationRule(
            steps[10].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.none_of_the_above) -> steps[7].id
                        else -> steps[9].id
                    }
                }
            )
        )

        task.setNavigationRule(
            steps[11].id,
            NavigationRule.DirectStepNavigationRule(
                destinationStepStepIdentifier = steps[12].id
            )
        )

        task.setNavigationRule(
            steps[12].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.yes) -> steps[9].id
                        else -> steps[13].id
                    }
                }
            )
        )

        task.setNavigationRule(
            steps[13].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.yes) -> steps[9].id
                        else -> steps[14].id
                    }
                }
            )
        )

        task.setNavigationRule(
            steps[14].id,
            NavigationRule.ConditionalDirectionStepNavigationRule(
                resultToStepIdentifierMapper = { input ->
                    when (input) {
                        this.resources.getString(R.string.none_of_the_above) -> steps[8].id
                        else -> steps[9].id
                    }
                }
            )
        )

        surveyView.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->
            if (reason == FinishReason.Completed) {
                taskResult.results.forEach { _ ->
                    binding.surveyContainer.removeAllViews()
                    findNavController().popBackStack(R.id.nav_assessment_fragment, false)
                }
            }
        }

        surveyView.start(task)
    }
}

class IntroScreening : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.intro_screening_fragment, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@IntroScreening.isOptional

            override var isOptional: Boolean = this@IntroScreening.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.continue_btn)
                    .setOnClickListener { onNextListener(createResults()) }
            }
        }
    }
}

class EmergencyStep : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.emergency_step_layout, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@EmergencyStep.isOptional

            override var isOptional: Boolean = this@EmergencyStep.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.ok_btn)
                    .setOnClickListener { onNextListener(createResults()) }

                root.findViewById<TextView>(R.id.text_info).text =
                    HtmlCompat.fromHtml(context.getString(R.string.emergency_message), 0)

            }
        }
    }
}

class Under18Step : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.under_18_layout, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@Under18Step.isOptional

            override var isOptional: Boolean = this@Under18Step.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.done_btn)
                    .setOnClickListener { onCloseListener(createResults(), FinishReason.Completed) }
            }
        }
    }
}

class ResultProtectYourself : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.result_protect_yourself, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@ResultProtectYourself.isOptional

            override var isOptional: Boolean = this@ResultProtectYourself.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.done_btn)
                    .setOnClickListener { onCloseListener(createResults(), FinishReason.Completed) }
            }
        }
    }
}

class ResultStayHome : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.result_stay_home, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@ResultStayHome.isOptional

            override var isOptional: Boolean = this@ResultStayHome.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.done_btn)
                    .setOnClickListener { onCloseListener(createResults(), FinishReason.Completed) }
            }
        }
    }
}

class ContactProvider : Step {
    override val isOptional: Boolean = false
    override val id: StepIdentifier =
        StepIdentifier()
    val tmp = id

    override fun createView(context: Context, stepResult: StepResult?): StepView {
        return object : StepView(context, id, isOptional) {

            override fun setupViews() = Unit

            val root = View.inflate(context, R.layout.result_contact_provider, this)

            override fun createResults(): QuestionResult = CustomResult(id = id)

            override fun isValidInput(): Boolean = this@ContactProvider.isOptional

            override var isOptional: Boolean = this@ContactProvider.isOptional
            override val id: StepIdentifier = tmp

            init {
                root.findViewById<Button>(R.id.done_btn)
                    .setOnClickListener { onCloseListener(createResults(), FinishReason.Completed) }
            }
        }
    }
}

@Parcelize
data class CustomResult(
    val customData: String = "",
    override val stringIdentifier: String = "stringIdentifier",
    override val id: Identifier,
    override val startDate: Date = Date(),
    override var endDate: Date = Date()
) : QuestionResult, Parcelable
