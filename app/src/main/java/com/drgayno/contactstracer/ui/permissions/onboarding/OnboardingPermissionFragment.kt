package com.drgayno.contactstracer.ui.permissions.onboarding

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.permissions.BasePermissionFragment

class OnboardingPermissionFragment :
    BasePermissionFragment<OnboardingPermissionViewModel>(
        R.layout.fragment_onboarding_permissions,
        OnboardingPermissionViewModel::class
    ) {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true)
    }
}