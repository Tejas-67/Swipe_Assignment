package com.tejas.swipe_assignment.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tejas.swipe_assignment.datamodel.ProductItem

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAllProductsFromLocal(): List<ProductItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(products: List<ProductItem>)

    @Query("DELETE FROM products")
    suspend fun clearProductTable()

    @Query("""
        SELECT *
        FROM products
        WHERE LOWER(product_name) LIKE '%' || LOWER(:query) || '%' OR 
        LOWER(:query) == product_type
    """)
    suspend fun searchProductTable(query: String): List<ProductItem>
}