package com.tejas.swipe_assignment.di

import android.app.Application
import com.tejas.swipe_assignment.util.NetworkConnectionLiveData
import com.tejas.swipe_assignment.ui.ProductAdapter
import com.tejas.swipe_assignment.network.ProductAPI
import com.tejas.swipe_assignment.repositories.ProductRepository
import com.tejas.swipe_assignment.room.ProductDatabase
import com.tejas.swipe_assignment.ui.ProductViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
        logging
    }
    single{
        OkHttpClient
            .Builder()
            .addInterceptor(
                get<HttpLoggingInterceptor>()
            )
            .build()
    }
    single {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ProductAPI::class.java)
    }

    single {
        ProductDatabase.getDatabase(get<Application>())
    }
    single{
        ProductRepository(db = get(), productAPI = get())
    }
    viewModel{
        ProductViewModel(get())
    }
    single{
        ProductAdapter()
    }
    single{
        NetworkConnectionLiveData(get<Application>())
    }
}
private val BASE_URL = "https://app.getswipe.in/api/public/"
