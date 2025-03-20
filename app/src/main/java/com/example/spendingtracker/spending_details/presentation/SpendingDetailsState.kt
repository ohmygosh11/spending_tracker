package com.example.spendingtracker.spending_details.presentation

import java.time.LocalDate
import java.time.ZonedDateTime

data class SpendingDetailsState(
    val name: String = "",
    val priceInput: String = "",
    val description: String = "",
    val dateTimeInput: String = LocalDate.now().toString()
)