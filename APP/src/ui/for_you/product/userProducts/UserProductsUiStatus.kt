package com.mcmp2023.s.ui.for_you.product.userProducts

import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.network.dto.login.LoginResponse
import com.mcmp2023.s.ui.account.login.LoginUiStatus
import java.lang.Exception

sealed class UserProductsUiStatus {
    object Resume : UserProductsUiStatus()

    class Error(val exception: Exception) : UserProductsUiStatus()

    data class ErrorWithMessage(val message: String) : UserProductsUiStatus()

    data class Success(val response: List<Product>) : UserProductsUiStatus()
}