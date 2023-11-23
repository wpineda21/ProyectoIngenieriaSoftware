package com.mcmp2023.s.ui.admin.adminprofiles

import com.mcmp2023.s.data.db.models.Product
import java.lang.Exception

sealed class AdminProfilesUiStatus {

    object Resume : AdminProfilesUiStatus()

    class Error(val exception: Exception) : AdminProfilesUiStatus()

    data class ErrorWithMessage(val message: String) : AdminProfilesUiStatus()

    data class Success(val response: List<Product>) : AdminProfilesUiStatus()
}