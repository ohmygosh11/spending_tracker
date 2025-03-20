package com.example.spendingtracker.spending_details.di


import com.example.spendingtracker.spending_details.domain.UpsertSpendingUseCase
import com.example.spendingtracker.spending_details.presentation.SpendingDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val spendingDetailsModule = module {
    factory { UpsertSpendingUseCase(get()) }
    viewModel { SpendingDetailsViewModel(get(), get()) }
}