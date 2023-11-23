package com.mcmp2023.s.repositories

import com.mcmp2023.s.data.db.ProductsDataBase
import com.mcmp2023.s.data.db.models.Product
import com.mcmp2023.s.network.ApiResponse
import com.mcmp2023.s.network.dto.sellproduct.SellProductRequest
import com.mcmp2023.s.network.dto.sellproduct.SellProductResponse
import com.mcmp2023.s.network.service.ProductService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class ProductRepository(
    private val api: ProductService,
    private val productsDataBase: ProductsDataBase
) {
    private val productDao = productsDataBase.productDao()
    suspend fun getProducts(): List<Product> {
        val response = api.getAllProducts()
        productDao.insertAll(response)
        return response
    }

    suspend fun getProductsByCategories(token: String, categoryName: String) =
        api.getProductsByCategory(token, categoryName)

    suspend fun searchProduct(productName: String) = api.searchProduct(productName)

    suspend fun sellProduct(
        token: String, image: MultipartBody.Part, titulo: String, description: String, pryce: Float, category: String, phoneNumber: String
    ): ApiResponse<String> {
        try {
            val response: SellProductResponse = api.sellProduct(
                token,
                image,
                RequestBody.create(MultipartBody.FORM,titulo),
                RequestBody.create(MultipartBody.FORM,description),
                RequestBody.create(MultipartBody.FORM,pryce.toString()),
                RequestBody.create(MultipartBody.FORM,category),
                RequestBody.create(MultipartBody.FORM,phoneNumber)
            )

            return ApiResponse.Success(response.message)
        } catch (e: HttpException) {
            if (e.code() == 400) {
                return ApiResponse.ErrorWithMessage("wrong data")
            }
            return ApiResponse.Error(e)
        } catch (e: IOException) {
            return ApiResponse.Error(e)
        }
    }

    fun getAll() = productDao.findAll()

}