package com.example.spendingtracker.spending_details.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendingtracker.core.domain.Spending
import com.example.spendingtracker.core.presentation.theme.SpendingTrackerTheme
import com.example.spendingtracker.core.presentation.theme.montserrat
import com.example.spendingtracker.core.presentation.utils.Background
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpendingDetailsScreenCore(
    viewModel: SpendingDetailsViewModel = koinViewModel(),
    spendingId: Int,
    onSaveSubmitClicked: () -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    LaunchedEffect(true) {
        viewModel.event.collect { event ->
            when (event) {
                SpendingDetailsEvent.SaveFailure -> {
                    Toast.makeText(
                        context,
                        "Save failed, please fill enough field",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                SpendingDetailsEvent.SaveSuccess -> {
                    Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    LaunchedEffect(spendingId) {
        viewModel.onAction(SpendingDetailsAction.LoadSpendingIfEditMode(spendingId))
    }

    SpendingDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onSaveSubmitClicked = {
            onSaveSubmitClicked()
            viewModel.onAction(SpendingDetailsAction.OnSubmitted(spendingId))
        },
        isEditMode = spendingId != -1,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpendingDetailsScreen(
    state: SpendingDetailsState,
    onAction: (SpendingDetailsAction) -> Unit,
    onSaveSubmitClicked: () -> Unit,
    isEditMode: Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(start = 6.dp, end = 12.dp),
                title = {
                    Text(
                        text = if (isEditMode) "Edit spending" else "Add a new spending",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = montserrat
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
                            .clickable(
                                onClick = onSaveSubmitClicked
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Background()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { onAction(SpendingDetailsAction.OnNameChanged(it)) },
                label = {
                    Text(
                        text = "Name",
                        fontFamily = montserrat,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )
            Spacer(Modifier.height(16.dp))

            Row {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = state.priceInput,
                    onValueChange = { it ->
                        onAction(SpendingDetailsAction.OnPriceChanged(it))
                    },
                    label = {
                        Text(
                            text = "Price",
                            fontFamily = montserrat,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(Modifier.width(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    value = state.dateTimeInput,
                    onValueChange = { onAction(SpendingDetailsAction.OnDateTimeChanged(it)) },
                    label = {
                        Text(
                            text = "Date",
                            fontFamily = montserrat,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                )
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { onAction(SpendingDetailsAction.OnDescriptionChanged(it)) },
                label = {
                    Text(
                        text = "Description",
                        fontFamily = montserrat,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )
        }

    }
}

@Preview
@Composable
private fun SpendingDetailsScreenPreview() {
    SpendingTrackerTheme() {
        SpendingDetailsScreen(
            state = SpendingDetailsState(),
            onAction = {},
            onSaveSubmitClicked = {},
            isEditMode = false
        )
    }
}