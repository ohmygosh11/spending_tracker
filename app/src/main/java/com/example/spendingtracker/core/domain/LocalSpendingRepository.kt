package com.example.spendingtracker.core.domain

import java.time.LocalDate

interface LocalSpendingRepository {

    suspend fun upsertSpending(spending: Spending)

    suspend fun deleteSpending(spendingId: Int)

    suspend fun getAllSpending(): List<Spending>

    suspend fun getAllDates(): List<LocalDate>

    suspend fun getSpendingByDate(date: LocalDate): List<Spending>

    suspend fun getSpendingById(spendingId: Int): Spending

    suspend fun getTotalSpent(): Double

}