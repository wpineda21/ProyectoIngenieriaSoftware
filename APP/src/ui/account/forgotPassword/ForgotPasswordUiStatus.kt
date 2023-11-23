package com.mcmp2023.s.ui.account.forgotPassword

import java.lang.Exception

sealed class ForgotPasswordUiStatus {
    object Resume : ForgotPasswordUiStatus()

    class Error(val exception: Exception) : ForgotPasswordUiStatus()

    data class ErrorWithMessage(val message: String) : ForgotPasswordUiStatus()

    object Success : ForgotPasswordUiStatus()
}
