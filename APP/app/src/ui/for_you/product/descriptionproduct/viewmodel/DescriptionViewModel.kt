package com.mcmp2023.s.ui.for_you.product.descriptionproduct.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mcmp2023.s.data.db.models.Product
import kotlin.math.log

class DescriptionViewModel : ViewModel() {
    var title = MutableLiveData("")
    var description = MutableLiveData("")
    var price = MutableLiveData("")
    var phoneNumber = MutableLiveData("")
    var imageUrl = ""

    fun setSelectedProduct(product: Product) {
        title.value = product.tittle
        description.value = product.description
        price.value = product.price
        phoneNumber.value = product.phoneNumber
        imageUrl = product.image.toString()
    }
}