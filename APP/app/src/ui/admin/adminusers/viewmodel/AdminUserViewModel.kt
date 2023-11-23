package com.mcmp2023.s.ui.admin.adminusers.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.repositories.adminrepo.AdminRepository
import com.mcmp2023.s.ui.admin.adminusers.AdminUserUiStatus
import kotlinx.coroutines.launch

class AdminUserViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    private val _status = MutableLiveData<AdminUserUiStatus>(AdminUserUiStatus.Resume)

    val status: MutableLiveData<AdminUserUiStatus>
        get() = _status

    private fun getUsers() {
        viewModelScope.launch {
            _status.postValue(
                when(val response = adminRepository.getUsers()){
                    is ApiResponse.Error -> AdminUserUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> AdminUserUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> AdminUserUiStatus.Success(response.data)
                }
            )
        }
    }

    fun onGetUsers() {
        getUsers()
    }


    suspend fun deleteUser(token: String, id: String) = adminRepository.deleteUsers(token, id)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
                AdminUserViewModel(app.userRepository)
            }
        }
    }

}