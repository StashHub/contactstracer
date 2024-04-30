package com.drgayno.contactstracer.ui.login

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.NavOptions.Builder
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.widget.addTextChangedListener
import com.drgayno.contactstracer.AppConfig
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.databinding.FragmentLoginBinding
import com.drgayno.contactstracer.ext.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import androidx.lifecycle.observe
import com.drgayno.contactstracer.util.*
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    R.layout.fragment_login, LoginViewModel::class
) {
    private val customTabHelper by inject<CustomTabHelper>()

    private lateinit var phoneNumberUtil: PhoneNumberUtil

    private lateinit var views: List<View>

    override fun onStart() {
        super.onStart()
        viewModel.state.observe(this) {
            updateState(it)
        }
        viewModel.verifyLaterShown.observe(this) {
            if (it) {
                login_verify_later_section.show()
            } else {
                login_verify_later_section.hide()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe(StartVerificationEvent::class) {
            verifyPhoneNumber()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.onboarding, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val handled = onBackPressed()
                return if (handled) {
                    true
                } else {
                    super.onOptionsItemSelected(item)
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Format number to include region code
        phoneNumberUtil = PhoneNumberUtil.createInstance(context)

        binding.loginVerifyPhoneInput.apply {
            addTextChangedListener(PhoneNumberFormattingTextWatcher())

            onTextChanged {
                binding.loginVerifyActivateBtn.isEnabled = it.isValidPhone(requireContext())
            }
        }

        views = listOf(
            login_progress,
            login_verify_activate_btn,
            login_verify_code_send_btn,
            error_message,
            error_image,
            login_verify_phone_input,
            login_verify_code_input,
            login_title,
            login_caption,
            login_verify_phone,
            login_verify_code,
            login_statement,
            error_button_back,
            phone_number_code,
            code_timeout,
            login_checkbox,
            login_verify_later_section,
            error_verify_later
        )

        setupListeners()

        enableUpInToolbar(true)

        login_statement.text = HtmlCompat.fromHtml(
            getString(R.string.login_statement, AppConfig.termsAndConditionsLink),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        login_statement.movementMethod = LinkMovementMethod.getInstance()
        login_statement.setOnClickListener { login_checkbox.toggle() }
    }

    private fun setupListeners() {
        login_statement.setOnClickListener {
            showWeb(AppConfig.termsAndConditionsLink, customTabHelper)
        }
        login_verify_later_button.setOnClickListener {
            login_verify_code_send_btn.hideKeyboard()
            viewModel.verifyLater()
        }
        error_verify_later.setOnClickListener {
            login_verify_code_send_btn.hideKeyboard()
            viewModel.verifyLater()
        }
        login_verify_phone_input.addTextChangedListener(afterTextChanged = {
            login_verify_phone.isErrorEnabled = false
        })
        login_verify_phone_input.setOnDoneListener {
            phoneNumberSent()
        }
        login_verify_activate_btn.setOnClickListener {
            phoneNumberSent()
        }
        login_verify_code_input.addTextChangedListener(afterTextChanged = {
            login_verify_code.isErrorEnabled = false
        })
        login_verify_code_input.setOnDoneListener {
            login_verify_code_send_btn.hideKeyboard()
            viewModel.codeEntered(login_verify_code_input.text.toString())
        }
        login_verify_code_send_btn.setOnClickListener {
            login_verify_code_send_btn.hideKeyboard()
            viewModel.codeEntered(login_verify_code_input.text.toString())
        }
        error_button_back.setOnClickListener {
            goBack()
        }
    }

    private fun phoneNumberSent() {
        if (login_checkbox.isChecked) {
            login_verify_activate_btn.hideKeyboard()
            viewModel.phoneNumberEntered(login_verify_phone_input.text.toString())
        } else {
            context?.let {
                MaterialAlertDialogBuilder(it)
                    .setMessage(R.string.consent_warning)
                    .setPositiveButton(R.string.Ok)
                    { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun updateState(state: LoginState) {
        when (state) {
            is EnterPhoneNumber -> {
                show(
                    login_title,
                    login_caption,
                    login_verify_phone,
                    login_verify_phone_input,
                    login_statement,
                    login_verify_activate_btn,
                    login_checkbox
                )
                login_verify_phone.isErrorEnabled = state.invalidPhoneNumber
                if (state.invalidPhoneNumber) {
                    login_verify_phone.error = getString(R.string.login_phone_input_error)
                }
            }
            is EnterCode -> {
                show(
                    phone_number_code,
                    login_verify_code_input,
                    login_verify_code,
                    login_verify_code_send_btn,
                    code_timeout
                )
                phone_number_code.text = getString(
                    R.string.login_phone_number_sms_sent,
                    state.phoneNumber.formatPhoneNumber()
                )
                login_verify_code.isErrorEnabled = state.invalidCode
                if (state.invalidCode) {
                    login_verify_code.error = getString(R.string.login_code_input_error)
                }
                login_verify_code_input.focusAndShowKeyboard()
            }
            is CodeReadAutomatically -> {
                show(
                    phone_number_code,
                    login_verify_code_input,
                    login_verify_code,
                    login_verify_code_send_btn,
                    code_timeout
                )
                phone_number_code.setText(R.string.login_code_read_automatically)
                login_verify_code.isErrorEnabled = false
                login_verify_code_input.setText(state.code)
            }
            SigningProgress -> show(login_progress)
            is LoginError -> showError(state)
            is SignedIn -> showSignedIn()
        }

        if (state is EnterCode || state is CodeReadAutomatically) {
            activity?.setTitle(R.string.login_verification_code)
        } else {
            activity?.title = ""
        }
    }

    private fun showSignedIn() {
        if (navController().currentDestination?.id == R.id.nav_login_fragment) {
            if (BatteryOptimization.isTutorialNeeded()) {
                navigate(
                    R.id.to_battery_opt_fragment, null,
                    Builder()
                        .setPopUpTo(
                            R.id.nav_graph,
                            true
                        ).build()
                )
            } else {
                navigate(
                    R.id.to_nav_dashboard_fragment, null,
                    Builder()
                        .setPopUpTo(
                            R.id.nav_graph,
                            true
                        ).build()
                )
            }
        }
    }

    private fun showError(error: LoginError) {
        error_message.text = error.text?.toCharSequence(requireContext())
        if (error.allowVerifyLater) {
            show(error_message, error_button_back, error_image, error_verify_later)
        } else {
            show(error_message, error_button_back, error_image)
        }
    }

    private fun verifyPhoneNumber() {
        val phoneNumberKit = PhoneNumberUtil.createInstance(context)
        val number = phoneNumberKit.parse(login_verify_phone_input.text.toString(), "US")
        val phoneNumber = phoneNumberKit.format(number, PhoneNumberUtil.PhoneNumberFormat.E164)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            AppConfig.smsTimeoutSeconds, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            requireActivity(), // Activity (for callback binding)
            viewModel.verificationCallbacks
        )
    }

    private fun show(vararg views: View) {
        views.forEach {
            it.visibility = View.VISIBLE
        }
        this.views.subtract(views.toList()).forEach {
            it.visibility = View.GONE
        }
    }

    private fun goBack() {
        view?.hideKeyboard()
        viewModel.backPressed()
    }

    override fun onBackPressed(): Boolean {
        return if (viewModel.state.value !is EnterPhoneNumber) {
            goBack()
            true
        } else false
    }
}
