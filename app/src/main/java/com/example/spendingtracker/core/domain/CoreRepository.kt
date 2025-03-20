package com.example.spendingtracker.core.domain

interface CoreRepository {

    suspend fun updateBalance(newBalance: Double)

    suspend fun getBalance(): Double
}