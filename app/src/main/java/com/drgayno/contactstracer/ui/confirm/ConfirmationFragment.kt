package com.drgayno.contactstracer.ui.confirm

import android.os.Bundle
import android.view.View
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentConfirmationBinding
import com.drgayno.contactstracer.ext.hide
import com.drgayno.contactstracer.ext.show
import com.drgayno.contactstracer.ext.withInternet
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.ui.confirm.event.CompletedEvent
import com.google.firebase.storage.StorageException
import com.drgayno.contactstracer.ui.confirm.event.ErrorEvent
import com.drgayno.contactstracer.ui.confirm.event.LogoutEvent
import com.drgayno.contactstracer.util.logoutWhenNotSignedIn

abstract class ConfirmationFragment :
    BaseFragment<FragmentConfirmationBinding, ConfirmationViewModel>(
        R.layout.fragment_confirmation,
        ConfirmationViewModel::class
    ) {

    abstract val titleRes: String
    abstract val description: String
    abstract val buttonTextRes: Int
    abstract fun confirmedClicked()
    abstract fun doWhenFinished()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribe(CompletedEvent::class) {
            doWhenFinished()
        }
        subscribe(ErrorEvent::class) {
            binding.apply {
                title.show()
                message.show()
                confirmButton.hide()
                confirmProgress.hide()
                message.text = when ((it.exception as? StorageException)?.errorCode) {
                    StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> getString(R.string.upload_error)
                    else -> getString(R.string.unexpected_error_text)
                }
            }
        }
        subscribe(LogoutEvent::class) {
            logoutWhenNotSignedIn()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true, IconType.UP)
        binding.apply {
            title.text = titleRes
            message.text = description
            confirmButton.setText(buttonTextRes)
            confirmButton.setOnClickListener {
                requireContext().withInternet {
                    title.hide()
                    message.hide()
                    confirmButton.hide()
                    confirmProgress.show()
                    confirmedClicked()
                }
            }
            title.show()
            message.show()
            confirmButton.show()
            confirmProgress.hide()
        }
    }
}