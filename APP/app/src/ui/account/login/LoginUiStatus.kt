package com.mcmp2023.s.ui.account.login

import com.mcmp2023.s.network.dto.login.LoginResponse
import java.lang.Exception

sealed class LoginUiStatus {
     object Resume : LoginUiStatus()
     class Error(val exception: Exception) : LoginUiStatus()
     data class ErrorWithMessage(val message: String) : LoginUiStatus()
     data class Success(val response: LoginResponse) : LoginUiStatus()
}