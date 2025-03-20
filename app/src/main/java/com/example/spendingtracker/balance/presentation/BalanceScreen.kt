package com.example.spendingtracker.balance.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendingtracker.core.presentation.theme.SpendingTrackerTheme
import com.example.spendingtracker.core.presentation.theme.montserrat
import com.example.spendingtracker.core.presentation.utils.Background
import org.koin.androidx.compose.koinViewModel

@Composable
fun BalanceScreenCore(
    viewModel: BalanceViewModel = koinViewModel(),
    onSaveClicked: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    BalanceScreen(
        state = state,
        onAction = viewModel::onAction,
        onSaveClicked = {
            viewModel.onAction(BalanceAction.OnBalanceSaved)
            onSaveClicked()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BalanceScreen(
    state: BalanceState,
    onAction: (BalanceAction) -> Unit,
    onSaveClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(start = 4.dp),
                title = {
                    Text(
                        text = "Update Balance",
                        fontFamily = montserrat,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                )
            )
        }
    )
    { paddingValues ->
        Background()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "$${state.balance}",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.balanceInput,
                onValueChange = {
                    onAction(BalanceAction.OnBalanceChanged(it))
                },
                label = {
                    Text(
                        text = "Enter balance",
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Medium,
                    )
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Spacer(Modifier.height(26.dp))

            OutlinedButton(
                onClick = onSaveClicked,
                modifier = Modifier.align(Alignment.End)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save balance",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Save balance",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 20.sp,
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun BalanceScreenPreview() {
    SpendingTrackerTheme {
        BalanceScreen(
            state = BalanceState(),
            onAction = {},
            onSaveClicked = {}
        )
    }
}