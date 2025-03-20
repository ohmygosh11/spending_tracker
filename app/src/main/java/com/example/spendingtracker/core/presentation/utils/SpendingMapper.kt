package com.example.spendingtracker.core.presentation.utils

import com.example.spendingtracker.core.data.local.SpendingEntity
import com.example.spendingtracker.core.domain.Spending
import java.time.Instant
import java.time.ZoneId

fun Spending.toSpendingEntity(): SpendingEntity {
    return SpendingEntity(
        spendingId = spendingId,
        name = name,
        price = price,
        description = description,
        dateTimeUtc = dateTimeUtc.toInstant().toString(),
    )
}

fun SpendingEntity.toSpending(): Spending {
    return Spending(
        spendingId = spendingId,
        name = name,
        price = price,
        description = description,
        dateTimeUtc = Instant.parse(dateTimeUtc).atZone(ZoneId.of("UTC")),
    )
}