package com.drgayno.contactstracer.ui.surveykit.result

import android.os.Parcelable
import com.drgayno.contactstracer.ui.surveykit.TaskIdentifier
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TaskResult(
    override val id: TaskIdentifier,
    override val startDate: Date,
    override var endDate: Date = Date(),
    val results: List<StepResult>
) : Result, Parcelable