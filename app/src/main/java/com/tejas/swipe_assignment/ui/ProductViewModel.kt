package com.tejas.swipe_assignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tejas.swipe_assignment.ProductScreenState
import com.tejas.swipe_assignment.Resource
import com.tejas.swipe_assignment.datamodel.ProductItem
import com.tejas.swipe_assignment.repositories.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
): ViewModel() {

    private val _productScreenState = MutableLiveData<ProductScreenState>(ProductScreenState())
    val productScreenState get() = _productScreenState

    init{
        getProducts()
    }

    fun getProducts(
        fetchFromRemote: Boolean = false
    ){
        _productScreenState.postValue(
            productScreenState.value!!.copy(
                isLoading = true
            )
        )
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