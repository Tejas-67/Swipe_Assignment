package com.tejas.swipe_assignment

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Success<T>(val data: T?): Resource<T>(data, null)
    class Loading<T>(val isLoading: Boolean = true): Resource<T>(null)
    class Error<T>(val message: String?, val data: T? = null): Resource<T>(data, message)
}