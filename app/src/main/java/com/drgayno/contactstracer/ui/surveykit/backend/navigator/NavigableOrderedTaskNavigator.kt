package com.drgayno.contactstracer.ui.surveykit.backend.navigator

import com.drgayno.contactstracer.ui.surveykit.NavigableOrderedTask
import com.drgayno.contactstracer.ui.surveykit.NavigationRule
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.steps.Step
import java.util.*

internal class NavigableOrderedTaskNavigator(
    override val task: NavigableOrderedTask
) : TaskNavigator {

    override var history: Stack<Step> = Stack()

    override fun startStep(stepResult: StepResult?): Step? {
        val previousStep = peekHistory()
        return if (previousStep == null) {
            task.steps.firstOrNull()
        } else {
            nextStep(previousStep, stepResult)
        }
    }

    override fun finalStep(): Step? = task.steps.lastOrNull()

    override fun nextStep(step: Step, stepResult: StepResult?): Step? {
        step.record()

        return when (val rule = step.extractRule() ?: return step.nextInList()) {
            is NavigationRule.DirectStepNavigationRule -> rule.evaluateNextStep()
            is NavigationRule.ConditionalDirectionStepNavigationRule ->
                rule.evaluateNextStep(step, stepResult)
        }
    }

    override fun previousStep(step: Step): Step? {
        return if (history.empty()) null
        else history.pop()
    }

    private fun Step?.record() {
        if (this != null) {
            history.push(this)
        }
    }

    private fun Step.extractRule(): NavigationRule? = task.getRule(this.id)

    private fun NavigationRule.DirectStepNavigationRule.evaluateNextStep() =
        task[this.destinationStepStepIdentifier]

    private fun NavigationRule.ConditionalDirectionStepNavigationRule.evaluateNextStep(
        step: Step,
        stepResult: StepResult?
    ): Step? {
        stepResult ?: return step.nextInList()
        val firstResult = stepResult.results.firstOrNull() ?: return step.nextInList()
        val nextStepIdentifier = this.resultToStepIdentifierMapper(firstResult.stringIdentifier)
            ?: return step.nextInList()

        return task[nextStepIdentifier]
    }
}