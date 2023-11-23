package com.mcmp2023.s.repositoires.credentialsrepo

import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.network.dto.forgotPassword.ForgotPasswordRequest
import com.mcmp2023.s.network.dto.forgotPassword.ForgotPasswordResponse
import com.mcmp2023.s.network.dto.restorePassword.RestorePasswordRequest
import com.mcmp2023.s.network.dto.restorePassword.RestorePasswordResponse
import com.mcmp2023.s.network.service.RestorePasswordService
import retrofit2.HttpException
import java.io.IOException

class RestorePasswordRepository(private val api: RestorePasswordService) {
    suspend fun forgotPassword(email: String): ApiResponse<String> {
        try {
            val response: ForgotPasswordResponse = api.forgotPassword(ForgotPasswordRequest(email))

            return ApiResponse.Success(response.message)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return ApiResponse.ErrorWithMessage("Email not found")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.Error(e)
        }
    }

    suspend fun restorePassword(
        email: String, code: String, newPassword: String
    ): ApiResponse<String> {
        try {
            val response: RestorePasswordResponse =
                api.restorePassword(RestorePasswordRequest(email, code, newPassword))

            return ApiResponse.Success(response.message)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return ApiResponse.ErrorWithMessage("Code or user not found")
            } else if (e.code() == 400) {
                return ApiResponse.ErrorWithMessage("Incorrect verification code")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.Error(e)
        }
    }
}