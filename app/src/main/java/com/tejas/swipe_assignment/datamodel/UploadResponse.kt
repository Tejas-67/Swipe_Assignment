package com.tejas.swipe_assignment.datamodel

data class UploadResponse(
    var message: String = "",
    var product_details: ProductItem,
    var product_id: String = "",
    var success: Boolean
)
