package com.tejas.swipe_assignment.repositories

import android.util.Log
import com.tejas.swipe_assignment.Resource
import com.tejas.swipe_assignment.datamodel.ProductItem
import com.tejas.swipe_assignment.datamodel.ProductItemResponse
import com.tejas.swipe_assignment.network.RetrofitInstance
import com.tejas.swipe_assignment.room.ProductDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository(
    private val db: ProductDatabase
) {
    private val productAPI = RetrofitInstance.productAPI

    val dao = db.getDao()
    private val _allProducts = dao.getAllProductsFromLocal()
    val allProducts get() = _allProducts

    suspend fun insertProduct(products: List<ProductItem>){
        dao.addProduct(products)
    }

    suspend fun clearProductTable(){
        dao.clearProductTable()
    }

    fun getProducts(
        fetchFromRemote: Boolean,
        onResponse: (Resource<ProductItemResponse>) -> Unit
    ){
        Log.w("products", "fetchFromRemote: $fetchFromRemote, allProducts: ${dao.getAllProductsFromLocal().isEmpty()}")
        if(fetchFromRemote || dao.getAllProductsFromLocal().isEmpty()){
            Log.w("products", "remote")
            val call = productAPI.getProducts()
            call.enqueue(object: Callback<ProductItemResponse>{
                override fun onResponse(
                    call: Call<ProductItemResponse>,
                    response: Response<ProductItemResponse>
                ) {
                    val result = response.body()
                    onResponse(
                        Resource.Success(result)
                    )
                }

                override fun onFailure(call: Call<ProductItemResponse>, t: Throwable) {
                    onResponse(
                        Resource.Error(t.message)
                    )
                }

            })
        }else{
            Log.w("products", "offline")
            val response = ProductItemResponse()
            response.addAll(dao.getAllProductsFromLocal())
            onResponse(Resource.Success(response))
        }
    }
}