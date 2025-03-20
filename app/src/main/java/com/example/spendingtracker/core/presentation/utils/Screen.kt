package com.example.spendingtracker.core.presentation.utils

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object SpendingOverviewScreen : Screen

    @Serializable
    data class SpendingDetailsScreen(val spendingId: Int = -1) : Screen

    @Serializable
    data object BalanceScreen : Screen
}