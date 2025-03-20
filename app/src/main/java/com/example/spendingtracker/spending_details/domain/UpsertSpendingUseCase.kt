package com.example.spendingtracker.spending_details.domain

import com.example.spendingtracker.core.domain.LocalSpendingRepository
import com.example.spendingtracker.core.domain.Spending

class UpsertSpendingUseCase(
    private val localSpendingRepository: LocalSpendingRepository
) {
    suspend operator fun invoke(spending: Spending): Boolean {
        if (spending.name.isBlank() || spending.price <= 0 || spending.dateTimeUtc.toString() == "" || spending.description.isBlank()) {
            return false
        }
        localSpendingRepository.upsertSpending(spending)
        return true
    }
}