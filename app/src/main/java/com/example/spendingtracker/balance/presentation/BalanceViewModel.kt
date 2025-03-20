package com.example.spendingtracker.balance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendingtracker.core.domain.CoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BalanceViewModel(
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BalanceState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    balance = coreRepository.getBalance()
                )
            }
        }
    }

    fun onAction(action: BalanceAction) {
        when (action) {
            is BalanceAction.OnBalanceChanged -> {
                _state.update {
                    it.copy(
                        balanceInput = action.balanceInput
                    )
                }
            }

            BalanceAction.OnBalanceSaved -> {
                viewModelScope.launch {
                    val balance = _state.value.balanceInput.toDoubleOrNull() ?: 0.0
                    coreRepository.updateBalance(balance)
                }
            }
        }
    }
}