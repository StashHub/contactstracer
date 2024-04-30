package com.drgayno.contactstracer.ui.success

import android.os.Bundle
import android.view.View
import com.drgayno.contactstracer.R

class SendDataSuccessFragment : SuccessFragment() {

    override val message = R.string.upload_data_success_description

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(true, IconType.CLOSE)
    }
}