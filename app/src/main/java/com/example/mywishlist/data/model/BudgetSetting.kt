package com.example.mywishlist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_settings")
data class BudgetSetting(
    @PrimaryKey val id: Int = 1,
    val dailyBudget: Long,
    val weeklyBudget: Long
)
