package com.drgayno.contactstracer.ui.surveykit.backend.navigator

import com.drgayno.contactstracer.ui.surveykit.OrderedTask
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.steps.Step
import java.util.*

internal class OrderedTaskNavigator(
    override val task: OrderedTask
) : TaskNavigator {

    override var history: Stack<Step> = Stack()

    override fun startStep(stepResult: StepResult?): Step? {
        val previousStep = peekHistory()
        return if (previousStep == null) task.steps.firstOrNull()
        else previousStep.nextInList()
    }

    override fun finalStep(): Step? = task.steps.lastOrNull()

    override fun nextStep(step: Step, stepResult: StepResult?): Step? = step.nextInList()

    override fun previousStep(step: Step): Step? = step.previousInList()

    private fun Step.previousInList(): Step? {
        val currentStepIndex = task.steps.indexOf(this)
        return if (currentStepIndex - 1 < 0) null
        else task.steps[currentStepIndex - 1]
    }
}