package com.example.spendingtracker.spending_overview.presentation

import java.time.LocalDate

sealed interface SpendingOverviewAction {
    data object LoadSpendingList : SpendingOverviewAction
    data class OnDateChanged(val date: LocalDate) : SpendingOverviewAction
    data class OnSpendingDeleted(val spendingId: Int) : SpendingOverviewAction
}