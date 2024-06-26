package com.drgayno.contactstracer.ui.dashboard

import android.content.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.databinding.FragmentPermissionsDisabledBinding
import com.drgayno.contactstracer.ext.hasLocationPermission
import com.drgayno.contactstracer.ext.isBatterySaverEnabled
import com.drgayno.contactstracer.ext.isLocationEnabled
import com.drgayno.contactstracer.ext.shareApp
import com.drgayno.contactstracer.service.CovidService
import com.drgayno.contactstracer.util.Auth
import com.drgayno.contactstracer.util.Log
import com.drgayno.contactstracer.util.logoutWhenNotSignedIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.koin.android.ext.android.inject


class DashboardFragment : BaseFragment<FragmentPermissionsDisabledBinding, DashboardViewModel>(
    R.layout.fragment_dashboard,
    DashboardViewModel::class
) {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var rxPermissions: RxPermissions
    private val localBroadcastManager by inject<LocalBroadcastManager>()

    private val serviceStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when (it.action) {
                    CovidService.ACTION_MASK_STARTED -> viewModel.serviceRunning.value = true
                    CovidService.ACTION_MASK_STOPPED -> viewModel.serviceRunning.value = false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.setTitle(R.string.app_name)
        registerServiceStateReceivers()
        rxPermissions = RxPermissions(this)
        subsribeToViewModel()
        viewModel.init()
        checkIfServiceIsRunning()
        checkIfSignedIn()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateState()
    }

    private fun subsribeToViewModel() {
        subscribe(DashboardCommandEvent::class) { commandEvent ->
            when (commandEvent.command) {
                DashboardCommandEvent.Command.TURN_ON -> tryStartBtService()
                DashboardCommandEvent.Command.TURN_OFF -> context?.let {
                    it.startService(CovidService.stopService(it))
                }
                DashboardCommandEvent.Command.PAUSE -> pauseService()
                DashboardCommandEvent.Command.RESUME -> resumeService()
                DashboardCommandEvent.Command.UPDATE_STATE -> updateState()
            }
        }
    }

    private fun updateState() {
        checkRequirements(onFailed = {
            navigate(R.id.nav_permissions_disabled)
        }, onBatterySaverEnabled = {
            showBatterySaverDialog()
        })
    }

    private fun showBatterySaverDialog() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setMessage(R.string.battery_saver_disabled_desc)
                .setPositiveButton(R.string.disable_battery_saver)
                { dialog, _ ->
                    dialog.dismiss()
                    navigateToBatterySaverSettings {
                        showSnackBar(R.string.battery_saver_settings_not_found)
                    }
                }
                .setNegativeButton(getString(R.string.Ok))
                { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun navigateToBatterySaverSettings(onBatterySaverNotFound: () -> Unit) {
        val batterySaverIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
        } else {
            val intent = Intent()
            intent.component = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$BatterySaverSettingsActivity"
            )
            intent
        }
        try {
            startActivity(batterySaverIntent)
        } catch (ex: ActivityNotFoundException) {
            onBatterySaverNotFound()
        }
    }

    private fun checkIfSignedIn() {
//        if (!Auth.isSignedIn()) {
//            logoutWhenNotSignedIn()
//        }
    }

    private fun checkIfServiceIsRunning() {
        if (CovidService.isRunning(requireContext())) {
            Log.d("Service Covid is running")
            viewModel.serviceRunning.value = true
        } else {
            viewModel.serviceRunning.value = false
            Log.d("Service Covid is not running")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableUpInToolbar(false)
        updateSecondaryText()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_share -> {
                requireContext().shareApp()
                true
            }
            R.id.nav_about -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBluetoothEnabled() {
        tryStartBtService()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        localBroadcastManager.unregisterReceiver(serviceStateReceiver)
        super.onDestroy()
    }

    private fun resumeService() {
        requireContext().run {
            startService(CovidService.resume(this))
        }
    }

    private fun updateSecondaryText() {
        if (Auth.isPhoneNumberVerified()) {
            app_running_body_secondary.text =
                getString(R.string.dashboard_secondary, viewModel.phoneNumber)
        } else {
            app_running_body_secondary.setText(R.string.dashboard_not_verified_secondary)
        }
    }

    private fun pauseService() {
        requireContext().run {
            startService(CovidService.pause(this))
        }
    }

    private fun registerServiceStateReceivers() {
        localBroadcastManager.registerReceiver(
            serviceStateReceiver,
            IntentFilter(CovidService.ACTION_MASK_STARTED)
        )
        localBroadcastManager.registerReceiver(
            serviceStateReceiver,
            IntentFilter(CovidService.ACTION_MASK_STOPPED)
        )
    }

    private fun checkRequirements(
        onPassed: () -> Unit = {},
        onFailed: () -> Unit = {},
        onBatterySaverEnabled: () -> Unit = {}
    ) {
        with(requireContext()) {
            if (viewModel.bluetoothRepository.hasBle(this)) {
                if (!viewModel.bluetoothRepository.isBtEnabled() || !isLocationEnabled() || !hasLocationPermission()) {
                    onFailed()
                    return
                } else if (isBatterySaverEnabled()) {
                    onBatterySaverEnabled()
                } else {
                    onPassed()
                }
            } else {
                showSnackBar(R.string.error_ble_unsupported)
            }
        }
    }

    private fun tryStartBtService() {
        checkRequirements(
            {
                ContextCompat.startForegroundService(
                    requireContext(),
                    CovidService.startService(requireContext())
                )
            },
            { navigate(R.id.nav_permissions_disabled) },
            { showBatterySaverDialog() }
        )
    }
}
