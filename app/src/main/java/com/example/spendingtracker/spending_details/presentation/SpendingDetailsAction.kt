package com.example.spendingtracker.spending_details.presentation

sealed interface SpendingDetailsAction {
    data class OnNameChanged(val name: String) : SpendingDetailsAction
    data class OnPriceChanged(val price: String) : SpendingDetailsAction
    data class OnDescriptionChanged(val description: String) : SpendingDetailsAction
    data class OnDateTimeChanged(val dateTime: String) : SpendingDetailsAction
    data class OnSubmitted(val spendingId: Int?) : SpendingDetailsAction
    data class LoadSpendingIfEditMode(val spendingId: Int) : SpendingDetailsAction
}