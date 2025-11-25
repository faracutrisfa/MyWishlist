package com.example.mywishlist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mywishlist.data.model.WishlistItem

@Database(
    entities = [WishlistItem::class],
    version = 1,
    exportSchema = false
)
abstract class WishlistDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao

    companion object {
        @Volatile
        private var INSTANCE: WishlistDatabase? = null

        fun getInstance(context: Context): WishlistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishlistDatabase::class.java,
                    "wishlist_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
