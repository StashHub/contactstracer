package com.drgayno.contactstracer.ui.surveykit.survey

import com.drgayno.contactstracer.ui.surveykit.FinishReason
import com.drgayno.contactstracer.ui.surveykit.Task
import com.drgayno.contactstracer.ui.surveykit.result.StepResult
import com.drgayno.contactstracer.ui.surveykit.result.TaskResult
import com.drgayno.contactstracer.ui.surveykit.steps.Step

internal interface Survey {
    var onStepResult: (Step?, StepResult?) -> Unit
    var onSurveyFinish: (TaskResult, FinishReason) -> Unit

    fun start(task: Task)
    fun backPressed()
}