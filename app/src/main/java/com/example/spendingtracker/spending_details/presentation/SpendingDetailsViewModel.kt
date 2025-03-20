package com.example.spendingtracker.spending_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendingtracker.core.domain.LocalSpendingRepository
import com.example.spendingtracker.core.domain.Spending
import com.example.spendingtracker.spending_details.domain.UpsertSpendingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

class SpendingDetailsViewModel(
    private val upsertSpendingUseCase: UpsertSpendingUseCase,
    private val localSpendingRepository: LocalSpendingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SpendingDetailsState())
    val state = _state.asStateFlow()

    private val _event = Channel<SpendingDetailsEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: SpendingDetailsAction) {
        when (action) {
            is SpendingDetailsAction.OnDateTimeChanged -> {
                _state.update {
                    it.copy(
                        dateTimeInput = action.dateTime,
                    )
                }
            }

            is SpendingDetailsAction.OnDescriptionChanged -> {
                _state.update {
                    it.copy(description = action.description)
                }
            }

            is SpendingDetailsAction.OnNameChanged -> {
                _state.update {
                    it.copy(name = action.name)
                }
            }

            is SpendingDetailsAction.OnPriceChanged -> {
                _state.update {
                    it.copy(
                        priceInput = action.price
                    )
                }
            }

            is SpendingDetailsAction.OnSubmitted -> {
                val spending = Spending(
                    spendingId = if (action.spendingId == -1) null else action.spendingId,
                    name = _state.value.name,
                    price = _state.value.priceInput.toDoubleOrNull() ?: 0.0,
                    description = _state.value.description,
                    dateTimeUtc = LocalDate.parse(_state.value.dateTimeInput)
                        .atStartOfDay(ZoneId.of("UTC")),
                )
                viewModelScope.launch {
                    val isSuccessful = upsertSpendingUseCase(spending)
                    if (isSuccessful) {
                        _event.send(SpendingDetailsEvent.SaveSuccess)
                    } else {
                        _event.send(SpendingDetailsEvent.SaveFailure)
                    }
                }
            }

            is SpendingDetailsAction.LoadSpendingIfEditMode -> {
                viewModelScope.launch {
                    if (action.spendingId != -1) {
                        val spending = localSpendingRepository.getSpendingById(action.spendingId)
                        _state.update {
                            it.copy(
                                name = spending.name,
                                priceInput = spending.price.toString(),
                                description = spending.description,
                                dateTimeInput = spending.dateTimeUtc.toLocalDate().toString()
                            )
                        }
                    }
                }
            }
        }
    }
}