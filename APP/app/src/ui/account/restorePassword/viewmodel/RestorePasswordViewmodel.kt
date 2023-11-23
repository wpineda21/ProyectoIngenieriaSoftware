package com.mcmp2023.s.ui.account.restorePassword.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mcmp2023.s.ProductApplication
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.repositoires.credentialsrepo.RestorePasswordRepository
import com.mcmp2023.s.ui.account.restorePassword.RestorePasswordUiStatus
import kotlinx.coroutines.launch

class RestorePasswordViewmodel(private val repository: RestorePasswordRepository) : ViewModel() {
    var email = MutableLiveData("")
    var code = MutableLiveData("")
    var newPassword = MutableLiveData("")

    private val _status = MutableLiveData<RestorePasswordUiStatus>(RestorePasswordUiStatus.Resume)

    val status: MutableLiveData<RestorePasswordUiStatus>
        get() = _status

    private fun restorePassword(email: String, code: String, newPassword: String) {
        viewModelScope.launch {
            _status.postValue(
                when (val response = repository.restorePassword(email, code, newPassword)) {
                    is ApiResponse.Error -> RestorePasswordUiStatus.Error(response.exception)
                    is ApiResponse.ErrorWithMessage -> RestorePasswordUiStatus.ErrorWithMessage(
                        response.message
                    )

                    is ApiResponse.Success -> RestorePasswordUiStatus.Success
                }
            )
        }
    }

    fun onRestorePassword() {
        if (!validateData()) {
            _status.value = RestorePasswordUiStatus.ErrorWithMessage("wrong data")
            return
        }
        restorePassword(email.value!!, code.value!!, newPassword.value!!)
    }

    private fun validateData(): Boolean {
        when {
            email.value.isNullOrEmpty() -> return false
            code.value.isNullOrEmpty() -> return false
            newPassword.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData() {
        email.value = ""
        code.value = ""
        newPassword.value = ""
    }

    fun clearStatus() {
        _status.value = RestorePasswordUiStatus.Resume
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as ProductApplication
                RestorePasswordViewmodel(app.restorePasswordRepository)
            }
        }
    }
}
