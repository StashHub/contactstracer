package com.drgayno.contactstracer.ui.surveykit.backend.views.step

interface Skipable {
    val isOptional: Boolean
    fun onSkip(block: () -> Unit)
}