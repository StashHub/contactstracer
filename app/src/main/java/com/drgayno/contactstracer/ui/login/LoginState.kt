package com.drgayno.contactstracer.ui.login

import com.drgayno.contactstracer.nav.event.LiveEvent
import com.drgayno.contactstracer.util.Text

sealed class LoginState
data class EnterPhoneNumber(val invalidPhoneNumber: Boolean) : LoginState()
data class EnterCode(val invalidCode: Boolean, val phoneNumber: String) : LoginState()
data class CodeReadAutomatically(val code: String) : LoginState()
object SigningProgress : LoginState()
data class LoginError(val text: Text?, val allowVerifyLater: Boolean) : LoginState()
object SignedIn : LoginState()

object StartVerificationEvent : LiveEvent()
