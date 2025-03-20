package com.example.spendingtracker.spending_overview.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendingtracker.core.domain.CoreRepository
import com.example.spendingtracker.core.domain.LocalSpendingRepository
import com.example.spendingtracker.core.domain.Spending
import com.example.spendingtracker.core.presentation.utils.randomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZonedDateTime

class SpendingOverviewViewModel(
    private val localSpendingRepository: LocalSpendingRepository,
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SpendingOverviewState())
    val state = _state.asStateFlow()

    fun onAction(action: SpendingOverviewAction) {
        when (action) {
            SpendingOverviewAction.LoadSpendingList -> {
                loadSpendingList()
            }

            is SpendingOverviewAction.OnDateChanged -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            spendingList = localSpendingRepository.getSpendingByDate(action.date),
                            pickedDate = action.date
                        )
                    }
                }
            }

            is SpendingOverviewAction.OnSpendingDeleted -> {
                viewModelScope.launch {
                    localSpendingRepository.deleteSpending(action.spendingId)

                    val pickedDate = _state.value.pickedDate
                    val spendingList = localSpendingRepository.getSpendingByDate(pickedDate)
                    if (spendingList.isNotEmpty()) {
                        _state.update {
                            it.copy(
                                spendingList = localSpendingRepository.getSpendingByDate(pickedDate).reversed(),
                                balance = coreRepository.getBalance() - localSpendingRepository.getTotalSpent()
                            )
                        }
                    } else {
                        loadSpendingList()
                    }

                }
            }
        }
    }

    fun loadSpendingList() {
        viewModelScope.launch {
            val dateList = localSpendingRepository.getAllDates()
            _state.update {
                it.copy(
                    spendingList = localSpendingRepository.getSpendingByDate(
                        dateList.maxOrNull() ?: LocalDate.now()
                    ).reversed(),
                    dateList = dateList,
                    pickedDate = dateList.maxOrNull() ?: LocalDate.now(),
                    balance = coreRepository.getBalance() - localSpendingRepository.getTotalSpent()
                )
            }
        }
    }

//    fun checkSpendingExist(): Boolean {
//        var allSpending: List<Spending> = emptyList()
//        viewModelScope.launch {
//            allSpending = localSpendingRepository.getAllSpending()
//        }
//        return allSpending.isEmpty()
//    }

}