package com.example.spendingtracker.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SpendingEntity::class], version = 1, exportSchema = false)
abstract class SpendingDatabase :RoomDatabase() {
    abstract val spendingDao: SpendingDao
}