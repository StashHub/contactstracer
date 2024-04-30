package com.drgayno.contactstracer.ui.base

import androidx.databinding.ViewDataBinding
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.nav.view.BaseArchActivity
import kotlin.reflect.KClass

open class BaseActivity<B : ViewDataBinding, VM : BaseViewModel>(
    layoutId: Int,
    viewModelClass: KClass<VM>
) :
    BaseArchActivity<B, VM>(layoutId, viewModelClass) {
    override fun onBackPressed() {
        val childFragmentManager =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager

        if ((childFragmentManager?.fragments?.get(0) as? BaseFragment<*, *>)?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }
}