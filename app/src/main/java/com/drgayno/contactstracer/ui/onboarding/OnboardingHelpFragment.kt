package com.drgayno.contactstracer.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.NavOptions
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.databinding.FragmentOnboardingHelpBinding
import com.drgayno.contactstracer.util.Auth

class OnboardingHelpFragment : BaseFragment<FragmentOnboardingHelpBinding, OnboardingViewModel>(
    R.layout.fragment_onboarding_help, OnboardingViewModel::class
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe(OnboardingEvent::class) {
            if (it.command == OnboardingEvent.Command.PREVENT) {
                navigate(R.id.to_nav_onboarding_prevent)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Auth.isSignedIn()) {
            navigate(
                R.id.to_nav_dashboard_fragment, null,
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.nav_graph,
                        true
                    ).build()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(false)
    }
}