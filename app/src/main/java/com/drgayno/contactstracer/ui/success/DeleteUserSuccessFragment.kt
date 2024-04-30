package com.drgayno.contactstracer.ui.success

import android.view.MenuItem
import com.drgayno.contactstracer.R

class DeleteUserSuccessFragment : SuccessFragment() {
    override val message = R.string.delete_user_success_text

    override fun onClose() {
        navigate(R.id.to_nav_onboarding_fragment)
    }

    override fun onBackPressed(): Boolean {
        onClose()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onClose()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}