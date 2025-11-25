package com.example.mywishlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mywishlist.data.model.WishlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist_items ORDER BY id DESC")
    fun getAllItems(): Flow<List<WishlistItem>>

    @Insert
    suspend fun insertItem(item: WishlistItem)

    @Update
    suspend fun updateItem(item: WishlistItem)
}