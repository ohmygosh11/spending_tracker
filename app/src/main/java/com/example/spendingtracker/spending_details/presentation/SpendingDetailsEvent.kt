package com.example.spendingtracker.spending_details.presentation

sealed interface SpendingDetailsEvent {
    object SaveSuccess: SpendingDetailsEvent
    object SaveFailure: SpendingDetailsEvent
}