package com.example.spendingtracker.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spendingtracker.balance.presentation.BalanceScreenCore
import com.example.spendingtracker.core.presentation.theme.SpendingTrackerTheme
import com.example.spendingtracker.core.presentation.utils.Screen
import com.example.spendingtracker.spending_details.presentation.SpendingDetailsScreenCore
import com.example.spendingtracker.spending_overview.presentation.SpendingOverviewScreenCore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendingTrackerTheme {
                Navigation(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun Navigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.SpendingOverviewScreen
    ) {
        composable<Screen.SpendingOverviewScreen> {
            SpendingOverviewScreenCore(
                onBalanceClicked = {
                    navController.navigate(Screen.BalanceScreen)
                },
                onAddSpendingClicked = {
                    navController.navigate(Screen.SpendingDetailsScreen())
                },
                onSpendingClicked = { spendingId ->
                    navController.navigate(Screen.SpendingDetailsScreen(spendingId))
                }
            )
        }

        composable<Screen.SpendingDetailsScreen> { backStackEntry ->
            val spendingId = backStackEntry.arguments?.getInt("spendingId") ?: -1
            SpendingDetailsScreenCore(
                spendingId = spendingId,
                onSaveSubmitClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.BalanceScreen> {
            BalanceScreenCore(
                onSaveClicked = {
                    navController.popBackStack()
                }
            )
        }
    }

}