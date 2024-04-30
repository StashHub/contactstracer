package com.drgayno.contactstracer.ui.surveykit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AnswerFormat {

    data class SingleChoiceAnswerFormat(
        val textChoices: List<TextChoice>,
        val defaultSelection: TextChoice? = null
    ) : AnswerFormat()

    data class MultipleChoiceAnswerFormat(
        val textChoices: List<TextChoice>,
        val defaultSelections: List<TextChoice> = emptyList()
    ) : AnswerFormat()
}

@Parcelize // necessary because it is used in QuestionResults (Single and Multiple)
data class TextChoice(
    val text: String,
    val value: String = text
) : Parcelable