package com.example.mywishlist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mywishlist.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}