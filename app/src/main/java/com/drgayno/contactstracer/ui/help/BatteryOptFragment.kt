package com.drgayno.contactstracer.ui.help

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.navigation.NavOptions
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.databinding.FragmentBatteryOptBinding
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.util.DeviceInfo
import org.koin.android.ext.android.inject

class BatteryOptFragment :
    BaseFragment<FragmentBatteryOptBinding, BatteryOptViewModel>(
        R.layout.fragment_battery_opt,
        BatteryOptViewModel::class
    ) {

    private val deviceInfo by inject<DeviceInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(false)
        activity?.setTitle(R.string.battery_opt_label)
        binding.doneBtn.setOnClickListener {
            navigate(
                R.id.to_nav_dashboard_fragment, null,
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.nav_graph,
                        true
                    ).build()
            )
        }
        binding.guideBtn.text = getString(R.string.guide_for, deviceInfo.getManufacturer())
        binding.guideBtn.setOnClickListener {
//            if (navController().currentDestination?.id == R.id.nav_battery_optimization) {
//                navigate(R.id.action_nav_battery_optimization_to_nav_guide, Bundle().apply {
//                    putBoolean("fullscreen", true)
//                })
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
