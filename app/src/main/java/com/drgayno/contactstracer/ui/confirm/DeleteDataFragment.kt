package com.drgayno.contactstracer.ui.confirm

import android.view.Menu
import android.view.MenuInflater
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.service.CovidService

class DeleteDataFragment : ConfirmationFragment() {
    override val titleRes by lazy { getString(R.string.delete_data) }
    override val description by lazy { getString(R.string.delete_data_description) }
    override val buttonTextRes = R.string.delete_data_button

    override fun confirmedClicked() {
        viewModel.deleteAllData()
    }

    override fun doWhenFinished() {
        context?.let {
            it.startService(
                CovidService.stopService(
                    context = it,
                    clearScanningData = true,
                    persistState = true
                )
            )
        }
        navigate(R.id.to_nav_delete_data_success)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
