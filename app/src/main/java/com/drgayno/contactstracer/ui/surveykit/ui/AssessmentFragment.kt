package com.drgayno.contactstracer.ui.surveykit.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentAssessmentBinding
import com.drgayno.contactstracer.ui.base.BaseFragment

class AssessmentFragment :
    BaseFragment<FragmentAssessmentBinding, AssessmentViewModel>(
        R.layout.fragment_assessment, AssessmentViewModel::class
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe(SurveyEvent::class) {
            when (it.command) {
                SurveyEvent.Command.START -> navigate(R.id.to_nav_survey_fragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(false)
    }
}
