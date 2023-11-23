package com.mcmp2023.s.ui.account.register

import java.lang.Exception

sealed class RegisterUiStatus {
    object Resume : RegisterUiStatus()

    class Error(val exception: Exception) : RegisterUiStatus()

    data class ErrorWithMessage(val message: String) : RegisterUiStatus()

    object Success : RegisterUiStatus()
}