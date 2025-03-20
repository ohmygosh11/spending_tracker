package com.example.spendingtracker.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SpendingDao {
    @Upsert
    suspend fun upsertSpending(spendingEntity: SpendingEntity)

    @Query("DELETE FROM spending WHERE spendingId = :spendingId")
    suspend fun deleteSpending(spendingId: Int)

    @Query("SELECT * FROM spending")
    suspend fun getAllSpending(): List<SpendingEntity>

    @Query("SELECT dateTimeUtc FROM spending")
    suspend fun getAllDates(): List<String>

    @Query("SELECT * FROM spending WHERE spendingId = :spendingId")
    suspend fun getSpendingById(spendingId: Int): SpendingEntity

    @Query("SELECT SUM(price) FROM spending")
    suspend fun getTotalSpent(): Double?


}