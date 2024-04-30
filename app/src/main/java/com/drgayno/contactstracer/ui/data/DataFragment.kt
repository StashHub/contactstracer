package com.drgayno.contactstracer.ui.data

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentDataBinding
import com.drgayno.contactstracer.ui.adapter.RecyclerLayoutStrategy
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DataFragment :
    BaseFragment<FragmentDataBinding, DataViewModel>(
        R.layout.fragment_data, DataViewModel::class
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(ExportEvent.PleaseWait::class) {
            showMessageDialog(getString(R.string.please_wait_upload, it.minutes))
        }
        subscribe(ExportEvent.Confirmation::class) {
            navigate(R.id.to_nav_send_data_fragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(false)
        binding.myDataInfo.text = getString(R.string.data_content_info, AppConfig.persistDataDays)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.data, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.data_description -> {
                showMessageDialog(
                    getString(
                        R.string.my_data_description,
                        AppConfig.persistDataDays
                    )
                )
                true
            }
            R.id.nav_send_data -> {
                viewModel.sendData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessageDialog(message: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(message)
                .setPositiveButton(getString(R.string.Ok))
                { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
