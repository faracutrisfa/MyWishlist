package com.example.mywishlist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val targetPrice: Long,
    val category: ItemCategory,
    val savedAmount: Long = 0L,
    val isPurchased: Boolean = false
) {
    val progress: Float
        get() = if (targetPrice <= 0) 0f
        else (savedAmount.toFloat() / targetPrice).coerceIn(0f, 1f)
}