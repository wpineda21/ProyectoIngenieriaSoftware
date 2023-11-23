package com.mcmp2023.s.repositories.credentialsrepo

import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.network.dto.login.LoginRequest
import com.mcmp2023.s.network.dto.login.LoginResponse
import com.mcmp2023.s.network.dto.register.RegisterRequest
import com.mcmp2023.s.network.dto.register.RegisterResponse
import com.mcmp2023.s.network.service.AuthService
import retrofit2.HttpException
import java.io.IOException

class CredentialsRepository(private val api: AuthService) {
    suspend fun login(email: String, password: String): ApiResponse<LoginResponse>{
        try{
            val response: LoginResponse =
                api.login(LoginRequest(email, password))

            //returns token and succes message
            return ApiResponse.Success(response)
        }catch(e: HttpException){
            //if code 400 return error 400 message
            if(e.code() == 400){
                return ApiResponse.ErrorWithMessage("Invalid email or Password")
            }
            return ApiResponse.Error(e)
        }catch(e: IOException){
            //CASE different error
            return ApiResponse.Error(e)
        }
    }

    suspend fun register(name: String,email: String, password: String) : ApiResponse<String>{
        try{
            val response: RegisterResponse = api.register(RegisterRequest(name, email, password))
            //returns succes message
            return ApiResponse.Success(response.message)
        }catch(e: HttpException){
            //if code 400 return error 400 message
            if(e.code() == 400){
                return ApiResponse.ErrorWithMessage("Invalid email or Password")
            }
            return ApiResponse.Error(e)
        }catch(e: IOException){
            //Case different error
            return ApiResponse.Error(e)
        }
    }
}