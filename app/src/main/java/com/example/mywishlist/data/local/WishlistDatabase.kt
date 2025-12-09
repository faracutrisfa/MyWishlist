package com.example.mywishlist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mywishlist.data.model.BudgetSetting
import com.example.mywishlist.data.model.Expense
import com.example.mywishlist.data.model.WishlistItem

@Database(
    entities = [
        WishlistItem::class,
        BudgetSetting::class,
        Expense::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WishlistDatabase : RoomDatabase() {

    abstract fun wishlistDao(): WishlistDao
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: WishlistDatabase? = null

        fun getInstance(context: Context): WishlistDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WishlistDatabase::class.java,
                    "wishlist_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}