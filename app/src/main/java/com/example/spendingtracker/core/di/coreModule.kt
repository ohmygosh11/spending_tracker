package com.example.spendingtracker.core.di

import androidx.room.Room
import com.example.spendingtracker.core.data.CoreRepositoryImpl
import com.example.spendingtracker.core.data.LocalSpendingRepositoryImpl
import com.example.spendingtracker.core.data.local.SpendingDatabase
import com.example.spendingtracker.core.domain.CoreRepository
import com.example.spendingtracker.core.domain.LocalSpendingRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val coreModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            SpendingDatabase::class.java,
            "spending_db"
        ).build()
    }

    single {
        get<SpendingDatabase>().spendingDao
    }

    single {
        androidApplication().getSharedPreferences(
            "spending_tracker",
            android.content.Context.MODE_PRIVATE
        )
    }

    single<LocalSpendingRepository> { LocalSpendingRepositoryImpl(get()) }

    single<CoreRepository> { CoreRepositoryImpl(get()) }

}