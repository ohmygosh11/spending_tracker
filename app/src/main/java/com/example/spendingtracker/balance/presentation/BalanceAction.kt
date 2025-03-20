package com.example.spendingtracker.balance.presentation

sealed interface BalanceAction {

    data class OnBalanceChanged(val balanceInput: String): BalanceAction

    data object OnBalanceSaved: BalanceAction

}