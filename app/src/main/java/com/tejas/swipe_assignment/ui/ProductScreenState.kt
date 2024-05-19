package com.tejas.swipe_assignment.ui

import com.tejas.swipe_assignment.datamodel.ProductItem

data class ProductScreenState(
    var isLoading: Boolean = false,
    var data: List<ProductItem> = listOf(),
    var error: String? = null
)
