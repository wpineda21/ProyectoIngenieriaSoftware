package com.mcmp2023.s.ui.account.restorePassword

import java.lang.Exception

sealed class RestorePasswordUiStatus {
    object Resume : RestorePasswordUiStatus()

    class Error(val exception: Exception) : RestorePasswordUiStatus()

    data class ErrorWithMessage(val message: String): RestorePasswordUiStatus()

    object Success : RestorePasswordUiStatus()
}