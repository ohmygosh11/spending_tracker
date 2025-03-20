package com.example.spendingtracker.spending_overview.presentation

import com.example.spendingtracker.core.domain.Spending
import java.time.LocalDate

data class SpendingOverviewState(
    val spendingList: List<Spending> = emptyList<Spending>(),
    val dateList: List<LocalDate> = emptyList<LocalDate>(),
    val pickedDate: LocalDate = LocalDate.now(),
    val balance: Double = 0.0,
)
