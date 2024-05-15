package com.tejas.swipe_assignment.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tejas.swipe_assignment.datamodel.ProductItem

@Database(
    entities = [ProductItem::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun getDao(): ProductDao

    companion object{
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase{
            if(INSTANCE!=null) return INSTANCE!!

            val instance = Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java,
                "database"
            )
                .allowMainThreadQueries()
                .build()

            INSTANCE = instance
            return INSTANCE!!
        }
    }
}