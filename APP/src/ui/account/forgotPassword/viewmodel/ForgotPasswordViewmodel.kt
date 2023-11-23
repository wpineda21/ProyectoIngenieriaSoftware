package com.mcmp2023.s.ui.account.forgotPassword.viewmodel

import android.app.Application
import android.text.Spannable.Factory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.repositoires.credentialsrepo.RestorePasswordRepository
import com.mcmp2023.s.ui.account.forgotPassword.ForgotPasswordUiStatus
import kotlinx.coroutines.launch

class ForgotPasswordViewmodel(private val repository: RestorePasswordRepository) : ViewModel() {
    var email = MutableLiveData("")

    private val _status = MutableLiveData<ForgotPasswordUiStatus>(ForgotPasswordUiStatus.Resume)

    val status: MutableLiveData<ForgotPasswordUiStatus>
        get() = _status

    private fun forgotPassword(email: String){
        viewModelScope.launch {
            _status.postValue(
                when(val response = repository.forgotPassword(email)){
                    is ApiResponse.Error -> ForgotPasswordUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> ForgotPasswordUiStatus.ErrorWithMessage(response.message)
                    is ApiResponse.Success -> ForgotPasswordUiStatus.Success
                }
            )
        }
    }

    fun onForgotPassword(){
        if(!validateData()){
            _status.value = ForgotPasswordUiStatus.ErrorWithMessage("wrong information")
            return
        }
        forgotPassword(email.value!!)
    }

    private fun validateData(): Boolean {
        when{
            email.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData(){
        email.value = ""
    }

    fun clearStatus(){
        _status.value = ForgotPasswordUiStatus.Resume
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
                ForgotPasswordViewmodel(app.restorePasswordRepository)
            }
        }
    }
}