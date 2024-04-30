package com.drgayno.contactstracer.ui.surveykit.backend.views.step

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.surveykit.StepIdentifier
import com.drgayno.contactstracer.ui.surveykit.backend.views.main_parts.*
import com.drgayno.contactstracer.ui.surveykit.backend.views.question_parts.InfoTextPart
import com.drgayno.contactstracer.ui.surveykit.result.QuestionResult
import java.util.*

abstract class QuestionView(
    context: Context,
    id: StepIdentifier,
    isOptional: Boolean,
    private val title: String?,
    private val text: String?,
    private val nextButtonText: String
) : StepView(context, id, isOptional), ViewActions {

    private val root: View = View.inflate(context, R.layout.view_question, this)
    var content: Content = root.findViewById(R.id.questionContent)
    var footer: Footer = content.findViewById(R.id.questionFooter)
    val startDate: Date = Date()

    abstract override fun createResults(): QuestionResult

    abstract override fun isValidInput(): Boolean

    @CallSuper
    override fun setupViews() {
        title?.let { InfoTextPart.title(context, it) }?.let(content::add)
        text?.let { InfoTextPart.info(context, it) }?.let(content::add)
    }

    override fun onViewCreated() {
        super.onViewCreated()
        footer.onBack = { onBackListener(createResults()) }
        footer.canContinue = isValidInput()
        footer.onContinue = { onNextListener(createResults()) }
        footer.setContinueButtonText(nextButtonText)
    }
}