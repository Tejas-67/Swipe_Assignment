package com.tejas.swipe_assignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tejas.swipe_assignment.ProductScreenState
import com.tejas.swipe_assignment.Resource
import com.tejas.swipe_assignment.datamodel.ProductItem
import com.tejas.swipe_assignment.repositories.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class ProductViewModel(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _productScreenState = MutableLiveData(ProductScreenState())
    val productScreenState get() = _productScreenState

    var searchJob: Job? = null
    val addProductResponse = MutableLiveData<Pair<Boolean, ProductItem?>>()

    init{
        getProducts()
    }


    fun addProduct(
        name: String,
        type: String,
        tax: String,
        price: String,
        file: File?
    ){
        productRepository.addProduct(name = name, type = type, tax = tax, amount = price, file = file){ success, product ->
            val res = Pair(success, product)
            addProductResponse.postValue(res)
        }
    }
    fun search(
        query: String
    ){
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            val filteredList = productRepository.search(query)
            _productScreenState.postValue(
                _productScreenState.value!!.copy(
                    data = filteredList
                )
            )
        }
    }

    fun getProducts(
        fetchFromRemote: Boolean = false
    ){
        viewModelScope.launch {
            _productScreenState.postValue(
                productScreenState.value!!.copy(
                    isLoading = true
                )
            )
            delay(2000)
            productRepository.getProducts(fetchFromRemote = fetchFromRemote) {
                when(it){
                    is Resource.Error -> {
                        _productScreenState.postValue(
                            productScreenState.value!!.copy(
                                error = it.message!!,
                                isLoading = false
                            )
                        )
                    }
                    is Resource.Success -> {
                        viewModelScope.launch {
                            productRepository.clearProductTable()
                            productRepository.insertProduct(it.data as List<ProductItem>)
                        }
                        _productScreenState.postValue(
                            productScreenState.value!!.copy(
                                isLoading = false,
                                error = null,
                                data = it.data as List<ProductItem>
                            )
                        )
                    }
                    else -> {}
                }
            }
        }
    }



}