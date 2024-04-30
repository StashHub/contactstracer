package com.drgayno.contactstracer.util

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.drgayno.contactstracer.R
import com.drgayno.contactstracer.ui.base.BaseFragment
import com.drgayno.contactstracer.db.SharedPrefsRepository
import com.drgayno.contactstracer.service.CovidService
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.KoinComponent
import org.koin.core.inject

object Auth : KoinComponent {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val prefs: SharedPrefsRepository by inject()

    fun isSignedIn(): Boolean {
        return auth.currentUser != null && prefs.getDeviceBuid() != null
    }

    fun isPhoneNumberVerified(): Boolean {
        return !auth.currentUser?.phoneNumber.isNullOrEmpty()
    }

    fun getFuid(): String {
        return checkNotNull(auth.currentUser?.uid)
    }

    fun getPhoneNumber(): String {
        val firebasePhoneNumber = auth.currentUser?.phoneNumber
        return if (firebasePhoneNumber.isNullOrEmpty()) {
            return prefs.getAuthPhoneNumber()
        } else {
            firebasePhoneNumber
        }
    }

    fun signOut() {
        auth.signOut()
    }
}

fun BaseFragment<*, *>.logoutWhenNotSignedIn() {
    with(requireContext()) {
        startService(
            CovidService.stopService(
                context = this,
                hideNotification = true,
                clearScanningData = true,
                persistState = false
            )
        )
    }
    Auth.signOut()

    val nav = findNavController()
    nav.popBackStack(R.id.nav_graph, false)
    nav.navigate(R.id.nav_onboarding_fragment)
    Toast.makeText(this.context, getString(R.string.automatic_logout_warning), Toast.LENGTH_LONG)
        .show()
}
