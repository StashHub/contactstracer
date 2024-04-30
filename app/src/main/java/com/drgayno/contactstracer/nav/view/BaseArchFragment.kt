package com.drgayno.contactstracer.nav.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.drgayno.contactstracer.BR
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.drgayno.contactstracer.ext.navigate
import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.nav.event.NavigationEvent
import com.drgayno.contactstracer.nav.viewmodel.BaseArchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseArchFragment<B : ViewDataBinding, VM : BaseArchViewModel>(
    @LayoutRes val layoutId: Int, viewModelClass: KClass<VM>
) : Fragment() {

    protected lateinit var binding: B
    open val viewModel: VM by viewModel(viewModelClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        subscribe(NavigationEvent::class) {
            navController().navigate(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.lifecycle, this)
        binding.setVariable(BR.viewModel, viewModel)
        return binding.root
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    protected fun <T : LiveEvent> subscribe(eventClass: KClass<T>, eventObserver: (T) -> Unit) {
        viewModel.subscribe(this, eventClass, Observer(eventObserver))
    }

    protected fun navController(): NavController {
        return NavHostFragment.findNavController(this)
    }

    protected fun navigate(
        @IdRes resId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null
    ) {
        navController().navigate(resId, args, navOptions)
    }

    protected fun navigate(directions: NavDirections, navOptions: NavOptions? = null) {
        navController().navigate(directions, navOptions)
    }
}
