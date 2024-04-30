package com.drgayno.contactstracer.ui.surveykit.result

import android.os.Parcelable
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class StepResult(
    override val id: StepIdentifier,
    override val startDate: Date,
    override var endDate: Date = Date(),
    var results: MutableList<QuestionResult> = mutableListOf()
) : Result, Parcelable