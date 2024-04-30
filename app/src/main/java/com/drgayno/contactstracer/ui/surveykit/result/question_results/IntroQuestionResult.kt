package com.drgayno.contactstracer.ui.surveykit.result.question_results

import android.os.Parcelable
import com.drgayno.contactstracer.ui.surveykit.Identifier
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class IntroQuestionResult(
    override val id: Identifier,
    override val startDate: Date,
    override var endDate: Date = Date(),
    override val stringIdentifier: String = ""
) : QuestionResult, Parcelable