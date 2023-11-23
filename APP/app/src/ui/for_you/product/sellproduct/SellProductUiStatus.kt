package com.mcmp2023.s.ui.for_you.product.sellproduct

import java.lang.Exception

sealed class SellProductUiStatus {
    object Resume : SellProductUiStatus()

    class Error(val exception: Exception) : SellProductUiStatus()

    data class ErrorWithMessage(val message: String) : SellProductUiStatus()

    object Success : SellProductUiStatus()
}