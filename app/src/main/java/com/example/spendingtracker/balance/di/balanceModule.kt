package com.example.spendingtracker.balance.di

import com.example.spendingtracker.balance.presentation.BalanceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val balanceModule = module {
    viewModel { BalanceViewModel(get()) }
}