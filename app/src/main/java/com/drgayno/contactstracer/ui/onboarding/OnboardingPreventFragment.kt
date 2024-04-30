package com.drgayno.contactstracer.ui.onboarding

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.navigation.NavOptions
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.databinding.FragmentOnboardingPreventBinding
import com.drgayno.contactstracer.util.Auth

class OnboardingPreventFragment :
    BaseFragment<FragmentOnboardingPreventBinding, OnboardingViewModel>(
        R.layout.fragment_onboarding_prevent, OnboardingViewModel::class
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe(OnboardingEvent::class) {
            when (it.command) {
                OnboardingEvent.Command.BLUETOOTH -> navigate(R.id.to_nav_onboarding_permissions)
                OnboardingEvent.Command.VERIFY -> navigate(R.id.to_nav_login_fragment)
                OnboardingEvent.Command.HELP -> { /* navigate to help activity */
                }
                OnboardingEvent.Command.PREVENT -> {/* do nothing */
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
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
        enableUpInToolbar(true)
    }
}