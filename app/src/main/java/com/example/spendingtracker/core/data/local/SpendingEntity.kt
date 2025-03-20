package com.example.spendingtracker.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "spending")
data class SpendingEntity(

    @PrimaryKey(autoGenerate = true)
    val spendingId: Int? = null,

    val name: String,
    val price: Double,
    val description: String,
    val dateTimeUtc: String
)
