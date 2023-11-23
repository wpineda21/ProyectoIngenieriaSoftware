package com.mcmp2023.s.ui.admin.adminprofiles.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.data.db.models.UserModel
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.repositories.adminrepo.AdminRepository
import com.mcmp2023.s.ui.admin.adminprofiles.AdminProfilesUiStatus
import kotlinx.coroutines.launch

class AdminProfilesViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    private val _status = MutableLiveData<AdminProfilesUiStatus>(AdminProfilesUiStatus.Resume)

    val status : MutableLiveData<AdminProfilesUiStatus>
        get() = _status

    var productName = ""

    var name = ""
    var id = ""

    fun setSelectedUser(user: UserModel) {
        id = user.ID.toString()
        Log.d("profile_id_string", id)
        Log.d("profile_id_string", user.toString())
        Log.d("profile_id", user.ID.toString())
        name = user.name
    }

    fun setSelectedProduct(product: Product) {
        productName = product.tittle
    }

   private fun getProductsByUser() {
       viewModelScope.launch {
           _status.postValue(
               when(val response = adminRepository.getProductsByUser(id)){
                   is ApiResponse.Error -> AdminProfilesUiStatus.Error(response.exception)
                   is ApiResponse.ErrorWithMessage -> AdminProfilesUiStatus.ErrorWithMessage(response.message)
                   is ApiResponse.Success -> AdminProfilesUiStatus.Success(response.data)
               }
           )
       }
   }

   fun onGetProductsByUser() {
       getProductsByUser()
   }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ProductApplication
                AdminProfilesViewModel(app.userRepository)
            }
        }
    }
}