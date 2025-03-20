package com.example.spendingtracker

import android.app.Application
import com.example.spendingtracker.balance.di.balanceModule
import com.example.spendingtracker.core.di.coreModule
import com.example.spendingtracker.spending_details.di.spendingDetailsModule
import com.example.spendingtracker.spending_overview.di.spendingOverviewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                coreModule,
                balanceModule,
                spendingOverviewModule,
                spendingDetailsModule
            )
        }
    }
}