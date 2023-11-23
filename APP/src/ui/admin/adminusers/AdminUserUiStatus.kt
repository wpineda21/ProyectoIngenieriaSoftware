package com.mcmp2023.s.ui.admin.adminusers


import com.mcmp2023.s.data.db.models.UserModel
import java.lang.Exception

sealed class AdminUserUiStatus {
    object Resume : AdminUserUiStatus()

    class Error(val exception: Exception) : AdminUserUiStatus()

    data class ErrorWithMessage(val message: String) : AdminUserUiStatus()

    data class Success(val response: List<UserModel>) : AdminUserUiStatus()
}
