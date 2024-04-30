package com.drgayno.contactstracer.ui.confirm

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.core.text.HtmlCompat
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.R

class SendDataFragment : ConfirmationFragment() {
    override val titleRes by lazy { getString(R.string.send_data) }
    override val description by lazy {
        getString(R.string.upload_confirmation)
    }
    override val buttonTextRes = R.string.yes_send
    override fun confirmedClicked() {
        viewModel.sendData()
    }

    override fun doWhenFinished() {
        navigate(R.id.to_nav_send_data_success)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = String.format(
            getString(R.string.upload_confirmation),
            AppConfig.termsAndConditionsLink
        )
        binding.message.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.message.movementMethod = LinkMovementMethod.getInstance()
    }
}
