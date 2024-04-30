package com.drgayno.contactstracer.ui.surveykit.backend.result

import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.result.TaskResult

interface ResultGatherer {
    val taskResult: TaskResult
    var results: MutableList<StepResult>

    fun store(stepResult: StepResult)
    fun retrieve(identifier: StepIdentifier): StepResult?
}