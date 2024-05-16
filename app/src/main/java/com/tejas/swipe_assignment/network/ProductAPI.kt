package com.tejas.swipe_assignment.network

import com.tejas.swipe_assignment.datamodel.ProductItemResponse
import com.tejas.swipe_assignment.datamodel.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductAPI  {

    @GET("get")
    fun getProducts(): Call<ProductItemResponse>

    @Multipart
    @POST("add")
    fun addProduct(
        @Part("product_name") name: RequestBody,
        @Part("product_type") type: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part?
    ): Call<UploadResponse>
}