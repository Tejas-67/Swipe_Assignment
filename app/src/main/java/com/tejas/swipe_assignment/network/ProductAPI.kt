package com.tejas.swipe_assignment.network

import com.tejas.swipe_assignment.datamodel.ProductItemResponse
import retrofit2.Call
import retrofit2.http.GET

interface ProductAPI  {

    @GET("get")
    fun getProducts(): Call<ProductItemResponse>
}