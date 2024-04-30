package com.drgayno.contactstracer.ui.surveykit.backend.result

import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.Task
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.result.TaskResult
import java.util.Date

internal class ResultGathererImpl(private val task: Task) : ResultGatherer {

    private val startDate: Date = Date()

    override var results: MutableList<StepResult> = mutableListOf()

    override val taskResult: TaskResult
        get() = TaskResult(
            id = task.id,
            results = results,
            startDate = startDate
        ).apply { endDate = Date() }

    override fun store(stepResult: StepResult) {
        val previousResult = retrieve(stepResult.id)
        if (previousResult == null) results.add(stepResult)
        else {
            val previousResultIndex = results.indexOf(previousResult)
            results[previousResultIndex] = stepResult
        }
    }

    override fun retrieve(identifier: StepIdentifier) = results.find { it.id == identifier }
}