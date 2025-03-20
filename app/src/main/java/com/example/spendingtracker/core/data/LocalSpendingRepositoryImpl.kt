package com.example.spendingtracker.core.data

import com.example.spendingtracker.core.data.local.SpendingDao
import com.example.spendingtracker.core.domain.LocalSpendingRepository
import com.example.spendingtracker.core.domain.Spending
import com.example.spendingtracker.core.presentation.utils.randomColor
import com.example.spendingtracker.core.presentation.utils.toSpending
import com.example.spendingtracker.core.presentation.utils.toSpendingEntity
import java.time.LocalDate
import java.time.ZonedDateTime

class LocalSpendingRepositoryImpl(
    private val spendingDao: SpendingDao
) : LocalSpendingRepository {
    override suspend fun upsertSpending(spending: Spending) {
        spendingDao.upsertSpending(spending.toSpendingEntity())
    }

    override suspend fun deleteSpending(spendingId: Int) {
        spendingDao.deleteSpending(spendingId)
    }

    override suspend fun getAllSpending(): List<Spending> {
        return spendingDao.getAllSpending().map {
            it.toSpending()
        }.map {
            it.copy(
                color = randomColor()
            )
        }
    }

    override suspend fun getAllDates(): List<LocalDate> {
        return spendingDao.getAllDates()
            .map {
                ZonedDateTime.parse(it).toLocalDate()
            }
            .distinct()
            .sortedDescending()
    }

    override suspend fun getSpendingByDate(date: LocalDate): List<Spending> {
        return getAllSpending()
            .filter {
                it.dateTimeUtc.toLocalDate() == date
            }
    }

    override suspend fun getSpendingById(spendingId: Int): Spending {
        return spendingDao.getSpendingById(spendingId).toSpending()
    }

    override suspend fun getTotalSpent(): Double {
        return spendingDao.getTotalSpent() ?: 0.0
    }

}