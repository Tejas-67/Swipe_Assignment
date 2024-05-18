package com.tejas.swipe_assignment.repositories

import android.util.Log
import com.tejas.swipe_assignment.util.Resource
import com.tejas.swipe_assignment.datamodel.ProductItem
import com.tejas.swipe_assignment.datamodel.ProductItemResponse
import com.tejas.swipe_assignment.datamodel.UploadResponse
import com.tejas.swipe_assignment.network.ProductAPI
import com.tejas.swipe_assignment.room.ProductDatabase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProductRepository(
    private val db: ProductDatabase,
    private val productAPI: ProductAPI
) {
    val dao = db.getDao()
    private val _allProducts = dao.getAllProductsFromLocal()
    val allProducts get() = _allProducts

    suspend fun insertProduct(products: List<ProductItem>){
        dao.addProduct(products)
    }

    suspend fun clearProductTable(){
        dao.clearProductTable()
    }

    suspend fun search(query: String): List<ProductItem>{
        return dao.searchProductTable(query)
    }

    fun addProduct(
        name: String,
        tax: String,
        amount: String,
        type: String,
        files: List<File>?,
        onResponse: (Boolean, ProductItem?) -> Unit
    ) {
        val fileParts = files?.map { file ->
            val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("files[]", file.name, requestBody)
        } ?: emptyList()

        val productNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val taxBody = RequestBody.create("text/plain".toMediaTypeOrNull(), tax)
        val amountBody = RequestBody.create("text/plain".toMediaTypeOrNull(), amount)
        val typeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), type)
        val call = productAPI.addProduct(name = productNameBody, tax = taxBody, price = amountBody, type = typeBody, files = fileParts)

        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                val result = response.body()
                onResponse(true, result?.product_details)
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                onResponse(false, null)
            }
        })
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