package com.drgayno.contactstracer.ui.surveykit.result

import android.os.Parcelable
import com.drgayno.contactstracer.ui.surveykit.Identifier
import java.util.*

interface Result : Parcelable {
    val id: Identifier
    val startDate: Date
    var endDate: Date
}