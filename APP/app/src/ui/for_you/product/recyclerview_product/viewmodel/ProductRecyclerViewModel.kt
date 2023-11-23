package com.mcmp2023.s.ui.for_you.product.recyclerview_product.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.R
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.repositories.ProductRepository
import kotlinx.coroutines.launch

class ProductRecyclerViewModel(private val productRepository: ProductRepository) : ViewModel() {

    var productName = ""

    private val _products = MutableLiveData<List<Product>>()

    val favoriteProduct : MutableList<Product> = mutableListOf()

    private val emptyList = mutableListOf<Product>()

    val products : LiveData<List<Product>> get() = _products

    var error = MutableLiveData<Int?>(null)

    fun fetchProducts(token: String, categoryName: String, name: String) {
        if (name.isNotBlank()) {
            searchProduct(name)
        } else if (token.isNotBlank() && categoryName.isNotBlank() ) {
            getProductsByCategory(token, categoryName)
        } else {
            getAllProducts()
        }
    }
    private fun getProductsByCategory(token: String, categoryName: String) {
       viewModelScope.launch {
           error.value = null
           try {
               if (categoryName.isNotBlank()) {
                   val response = productRepository.getProductsByCategories("Bearer $token", categoryName)
                   _products.value = response.products
                   Log.d("ViewModel", products.toString())
               } else   {
                    error.value = R.string.error_empty_text
               }
           } catch (e: Exception) {
               Log.e("ViewModel", "Error al buscar", e)
           }
       }
    }

    private fun searchProduct(name: String) {
        viewModelScope.launch {
            try {
                if (name.isNotBlank()) {
                    val response = productRepository.searchProduct(name)
                    _products.value = response
                    Log.d("ViewModel", products.toString())
                } else   {
                    error.value = R.string.error_empty_text
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error al buscar", e)
            }
        }
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            try {
                val response = productRepository.getProducts()
                _products.value = response
            } catch (e: Exception) {
                Log.e("ViewModel", "Error al buscar", e)
            }
        }
    }

    fun toggleFavorites(product: Product) {
        if (favoriteProduct.contains(product)) {
            favoriteProduct.remove(product)
        } else {
            favoriteProduct.add(product)
        }
    }

    fun clearProductName() {
        productName = ""
    }

    fun clearProducts() {
        _products.value = emptyList
    }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
                ProductRecyclerViewModel(app.productRepository)
            }
        }
    }

}