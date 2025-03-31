package com.example.spendingtracker.spending_overview.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendingtracker.core.domain.Spending
import com.example.spendingtracker.core.presentation.theme.SpendingTrackerTheme
import com.example.spendingtracker.core.presentation.theme.montserrat
import com.example.spendingtracker.core.presentation.utils.Background
import com.example.spendingtracker.core.presentation.utils.randomColor
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun SpendingOverviewScreenCore(
    viewModel: SpendingOverviewViewModel = koinViewModel(),
    onBalanceClicked: () -> Unit,
    onAddSpendingClicked: () -> Unit,
    onSpendingClicked: (Int) -> Unit
) {

    LaunchedEffect(true) {
        viewModel.onAction(SpendingOverviewAction.LoadSpendingList)
    }

    var state = viewModel.state.collectAsState().value
    SpendingOverviewScreen(
        state = state,
        onAction = viewModel::onAction,
        onBalanceClicked = onBalanceClicked,
        onAddSpendingClicked = onAddSpendingClicked,
        onSpendingDeleted = { spendingId ->
            viewModel.onAction(SpendingOverviewAction.OnSpendingDeleted(spendingId))
        },
        onSpendingClicked = onSpendingClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpendingOverviewScreen(
    state: SpendingOverviewState,
    onAction: (SpendingOverviewAction) -> Unit,
    onBalanceClicked: () -> Unit,
    onAddSpendingClicked: () -> Unit,
    onSpendingDeleted: (Int) -> Unit,
    onSpendingClicked: (Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                ScreenTopBar(
                    state = state,
                    scrollBehavior = scrollBehavior,
                    onBalanceClicked = onBalanceClicked,
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(end = 4.dp)
                )
                DatePickerDropdownMenu(
                    state = state,
                    onDateChanged = { date ->
                        onAction(SpendingOverviewAction.OnDateChanged(date))
                    }
                )
            }
        },
        floatingActionButton = {
            Column {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.inversePrimary.copy(0.3f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(0.2f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .clickable(
                            onClick = onAddSpendingClicked
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    ) { paddingValues ->

        Background()

        SpendingList(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onSpendingDeleted = onSpendingDeleted,
            onSpendingClicked = onSpendingClicked
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpendingList(
    modifier: Modifier = Modifier,
    state: SpendingOverviewState,
    onSpendingDeleted: (Int) -> Unit,
    onSpendingClicked: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp),
    ) {
        items(state.spendingList) { spending ->
            SpendingItem(
                spending = spending,
                onSpendingDeleted = onSpendingDeleted,
                onSpendingClicked = onSpendingClicked
            )

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpendingItem(
    modifier: Modifier = Modifier,
    spending: Spending,
    onSpendingDeleted: (Int) -> Unit,
    onSpendingClicked: (Int) -> Unit
) {
    var isShowDelete by remember { mutableStateOf(false) }
    Box {
        ElevatedCard(
            modifier = Modifier
                .height(148.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onSpendingClicked(spending.spendingId ?: 0)
                    },
                    onLongClick = {
                        isShowDelete = true
                    }
                ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(randomColor()))
                    .padding(vertical = 16.dp, horizontal = 20.dp)

            ) {
                Text(
                    text = spending.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = montserrat,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Price: ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = montserrat,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                    )
                    Text(
                        text = "$${spending.price}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = montserrat,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Note: ${spending.description}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = montserrat,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                )

            }
        }
        DropdownMenu(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.error.copy(0.9f),
                shape = RoundedCornerShape(8.dp)
            ),
            expanded = isShowDelete,
            onDismissRequest = { isShowDelete = false },
            offset = DpOffset(28.dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Delete",
                            fontSize = 16.sp,
                            fontFamily = montserrat,
                            color = MaterialTheme.colorScheme.onError,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.onError
                        )

                    }
                },
                onClick = {
                    isShowDelete = false
                    onSpendingDeleted(spending.spendingId ?: -1)
                }
            )
        }
    }
    Spacer(Modifier.height(16.dp))
}


@Composable
fun DatePickerDropdownMenu(
    modifier: Modifier = Modifier,
    state: SpendingOverviewState,
    onDateChanged: (LocalDate) -> Unit
) {
    var isExpandedDatePicker by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceDim.copy(0.8f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .clickable(
                    onClick = { isExpandedDatePicker = !isExpandedDatePicker }
                ),
        ) {
            Text(
                text = " ${state.pickedDate}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserrat,
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
            )
        }

        DropdownMenu(
            modifier = Modifier.heightIn(max = 400.dp),
            expanded = isExpandedDatePicker,
            onDismissRequest = { isExpandedDatePicker = false },
            offset = DpOffset(12.dp, 2.dp)
        ) {
            state.dateList.forEach { date ->
                DropdownMenuItem(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = {
                        Text(
                            fontSize = 16.sp,
                            text = "$date",
                            fontFamily = montserrat
                        )
                    },
                    onClick = {
                        isExpandedDatePicker = false
                        onDateChanged(date)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    state: SpendingOverviewState,
    onBalanceClicked: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = "$${state.balance}",
                fontWeight = FontWeight.SemiBold,
                fontFamily = montserrat,
                fontSize = 36.sp,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .background(MaterialTheme.colorScheme.inversePrimary.copy(0.3f))
                    .clickable(
                        onClick = onBalanceClicked
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}

@Preview
@Composable
private fun SpendingOverviewScreenPreview() {
    SpendingTrackerTheme() {
        SpendingOverviewScreen(
            state = SpendingOverviewState(),
            onAction = {},
            onAddSpendingClicked = {},
            onBalanceClicked = {},
            onSpendingDeleted = {},
            onSpendingClicked = {}
        )
    }
}