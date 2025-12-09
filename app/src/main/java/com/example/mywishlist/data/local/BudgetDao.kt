package com.example.mywishlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mywishlist.data.model.BudgetSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budget_settings WHERE id = 1")
    fun getBudgetSetting(): Flow<BudgetSetting?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(setting: BudgetSetting)
}
