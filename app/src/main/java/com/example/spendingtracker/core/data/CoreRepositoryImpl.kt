package com.example.spendingtracker.core.data

import android.content.SharedPreferences
import com.example.spendingtracker.core.domain.CoreRepository
import androidx.core.content.edit

class CoreRepositoryImpl(
    private val prefs: SharedPreferences
): CoreRepository {
    override suspend fun updateBalance(newBalance: Double) {
        prefs.edit() { putFloat(BALANCE_KEY, newBalance.toFloat()) }
    }

    override suspend fun getBalance(): Double {
        return prefs.getFloat(BALANCE_KEY, 0.0f).toDouble()
    }

    companion object {
        const val BALANCE_KEY = "balance"
    }
}