package com.drgayno.contactstracer.ui.permissions

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.drgayno.contactstracer.R

class PermissionDisabledFragment : BasePermissionFragment<PermissionDisabledViewModel>(
    R.layout.fragment_permissions_disabled, PermissionDisabledViewModel::class
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableUpInToolbar(false)
        viewModel.initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
        // return when (item.itemId) {
//            R.id.menu_share -> {
//                requireContext().shareApp()
//                true
//            }
//            R.id.nav_about -> {
//                navigate(R.id.nav_about)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
        // }
    }
}