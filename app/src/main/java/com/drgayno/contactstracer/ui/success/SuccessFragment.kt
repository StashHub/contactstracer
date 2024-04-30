package com.drgayno.contactstracer.ui.success

import android.os.Bundle
import android.view.View
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentSuccessBinding
import com.drgayno.contactstracer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_success.*

abstract class SuccessFragment :
    BaseFragment<FragmentSuccessBinding, SuccessViewModel>(
        R.layout.fragment_success,
        SuccessViewModel::class
    ) {

    open val message: Int? = null
    open fun onClose() {
        navController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true, IconType.UP)
        message?.let { binding.successDescription.setText(it) }
        binding.closeButton.setOnClickListener {
            onClose()
        }
    }
}
