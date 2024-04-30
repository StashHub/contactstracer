package com.drgayno.contactstracer.ui.confirm

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.service.CovidService
import com.drgayno.contactstracer.util.Auth
import com.drgayno.contactstracer.util.formatPhoneNumber

class DeleteUserFragment : ConfirmationFragment() {
    override val titleRes by lazy { getString(R.string.delete_registration) }
    override val description by lazy {
        if (Auth.isPhoneNumberVerified()) {
            getString(R.string.delete_user_desc, Auth.getPhoneNumber().formatPhoneNumber())
        } else {
            getString(R.string.delete_user_desc_unverified)
        }
    }
    override val buttonTextRes = R.string.delete_data_button
    override fun confirmedClicked() {
        viewModel.deleteUser()
    }

    override fun doWhenFinished() {
        context?.let {
            it.startService(
                CovidService.stopService(
                    context = it,
                    hideNotification = true,
                    clearScanningData = true,
                    persistState = false
                )
            )
        }
        navigate(R.id.to_nav_delete_user_success)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true, IconType.CLOSE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
