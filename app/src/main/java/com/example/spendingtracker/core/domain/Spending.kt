package com.example.spendingtracker.core.domain

import java.time.ZonedDateTime

data class Spending(
    val spendingId: Int?,
    val name: String,
    val price: Double,
    val description: String,
    val dateTimeUtc: ZonedDateTime,
    val color: Int = 0
)
