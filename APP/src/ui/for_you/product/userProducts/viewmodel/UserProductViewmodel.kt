package com.mcmp2023.s.ui.for_you.product.userProducts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.repositories.adminrepo.AdminRepository
import com.mcmp2023.s.ui.for_you.product.userProducts.UserProductsUiStatus
import kotlinx.coroutines.launch

class UserProductViewmodel(private val repository: AdminRepository): ViewModel() {

    private val _status = MutableLiveData<UserProductsUiStatus>(UserProductsUiStatus.Resume)

    val status: MutableLiveData<UserProductsUiStatus>
        get() = _status


    private fun getUserProducts(token: String){
        viewModelScope.launch {
            _status.postValue(
                when(val response = repository.getUserProducts("Bearer $token")){
                    is ApiResponse.Error -> UserProductsUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> UserProductsUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> UserProductsUiStatus.Success(response.data)
                }
            )
        }
    }

    fun onGetUserProducts(token: String){
        getUserProducts(token)
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
                UserProductViewmodel(app.userRepository)
            }
        }
    }
}