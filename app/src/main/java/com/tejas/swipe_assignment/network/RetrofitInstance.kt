package com.tejas.swipe_assignment.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance{
    companion object{
        private val BASE_URL = "https://app.getswipe.in/api/public/"

        val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client= OkHttpClient.Builder().addInterceptor(logging).build()

            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val productAPI by lazy {
            retrofit.create(ProductAPI::class.java)
        }
    }
}