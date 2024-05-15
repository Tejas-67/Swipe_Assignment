package com.tejas.swipe_assignment.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="products")
data class ProductItem(
    var image: String = "",
    var price: Float = 0.0f,
    var product_name: String = "",
    var product_type: String = "",
    var tax: Float = 0.0f,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
