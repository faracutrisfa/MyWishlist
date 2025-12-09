package com.example.mywishlist.ui.screen.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.local.WishlistDatabase
import com.example.mywishlist.data.model.BudgetSetting
import com.example.mywishlist.data.model.Expense
import com.example.mywishlist.ui.components.EmptyState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TextPrimary = Color(0xFF241E49)
private val TextSecondary = Color(0xFF6D6A7C)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen() {

    val context = LocalContext.current
    val db = remember { WishlistDatabase.getInstance(context) }
    val budgetDao = remember { db.budgetDao() }
    val expenseDao = remember { db.expenseDao() }
    val scope = rememberCoroutineScope()

    var budgetSetting by remember { mutableStateOf<BudgetSetting?>(null) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }

    var dailyBudgetInput by remember { mutableStateOf("") }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<Expense?>(null) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        budgetDao.getBudgetSetting().collectLatest { setting ->
            budgetSetting = setting
            if (setting != null && dailyBudgetInput.isBlank()) {
                dailyBudgetInput = setting.dailyBudget.toString()
            }
        }
    }

    LaunchedEffect(Unit) {
        expenseDao.getAllExpenses().collectLatest { list ->
            expenses = list
        }
    }

    val dailyBudget = budgetSetting?.dailyBudget ?: dailyBudgetInput.toLongOrNull() ?: 0L
    val weeklyBudget = budgetSetting?.weeklyBudget ?: dailyBudget * 7
    val spentTotal = remember(expenses) { expenses.sumOf { it.amount } }
    val remaining = (weeklyBudget - spentTotal).coerceAtLeast(0L)
    val progress = if (weeklyBudget > 0) spentTotal.toFloat() / weeklyBudget.toFloat() else 0f

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Budget Control",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExpenseDialog = true },
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah pengeluaran")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SummaryHeader(
                    weeklyBudget = weeklyBudget,
                    dailyBudget = dailyBudget,
                    totalSpent = spentTotal,
                    remaining = remaining,
                    progress = progress
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(18.dp),
                            ambientColor = Color(0x22000000),
                            spotColor = Color(0x22000000)
                        ),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = dailyBudgetInput,
                            onValueChange = {
                                dailyBudgetInput = it.filter { c -> c.isDigit() }
                            },
                            label = {
                                Text(
                                    "Budget harian (Rp)",
                                    color = TextSecondary
                                )
                            },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                disabledTextColor = TextPrimary,
                                focusedBorderColor = Color(0xFF6750A4),
                                unfocusedBorderColor = Color(0xFFCAC4D0),
                                focusedLabelColor = Color(0xFF6750A4),
                                unfocusedLabelColor = TextSecondary,
                                cursorColor = Color(0xFF6750A4)
                            )
                        )

                        Button(
                            onClick = {
                                val daily = dailyBudgetInput.toLongOrNull() ?: 0L
                                val weekly = daily * 7
                                scope.launch {
                                    budgetDao.upsert(
                                        BudgetSetting(
                                            id = 1,
                                            dailyBudget = daily,
                                            weeklyBudget = weekly
                                        )
                                    )
                                }
                            },
                            enabled = dailyBudgetInput.toLongOrNull() != null,
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6750A4),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Simpan")
                        }
                    }
                }

                Text(
                    text = "Pengeluaran yang tercatat",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )

                if (expenses.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(expenses, key = { it.id }) { expense ->
                            ExpenseItemRow(
                                expense = expense,
                                onDelete = { deleteTarget = expense }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddExpenseDialog) {
        AddExpenseDialog(
            onDismiss = { showAddExpenseDialog = false },
            onConfirm = { name, amount ->
                scope.launch {
                    expenseDao.insert(
                        Expense(
                            name = name,
                            amount = amount
                        )
                    )
                }
                showAddExpenseDialog = false
            }
        )
    }

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Hapus pengeluaran", color = TextPrimary) },
            text = { Text("Yakin ingin menghapus \"${target.name}\"?", color = TextSecondary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch { expenseDao.delete(target) }
                        deleteTarget = null
                    }
                ) {
                    Text("Hapus", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Batal", color = TextPrimary)
                }
            }
        )
    }
}

@Composable
private fun ExpenseItemRow(
    expense: Expense,
    onDelete: () -> Unit
) {
    val TextPrimary = Color(0xFF241E49)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = expense.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Rp ${expense.amount}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF6750A4)
                    )
                )
            }

            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFFB3261E)
                )
            ) {
                Text("Hapus")
            }
        }
    }
}

@Composable
private fun SummaryHeader(
    weeklyBudget: Long,
    dailyBudget: Long,
    totalSpent: Long,
    remaining: Long,
    progress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x22000000),
                spotColor = Color(0x22000000)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6750A4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6750A4),
                            Color(0xFF4A3DB5),
                            Color(0xFF8E63F4)
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ringkasan budget",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Harian",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFEADDFF)
                        )
                    )
                    Text(
                        text = if (dailyBudget == 0L) "—" else "Rp $dailyBudget",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Mingguan",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFEADDFF)
                        )
                    )
                    Text(
                        text = if (weeklyBudget == 0L) "—" else "Rp $weeklyBudget",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Sisa",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFEADDFF)
                        )
                    )
                    Text(
                        text = "Rp $remaining",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFFFD8E4),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Color(0xFFFFD8E4),
                trackColor = Color.White.copy(alpha = 0.16f)
            )

            Text(
                text = "Terpakai: Rp $totalSpent",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFFF5F5FF)
                )
            )
        }
    }
}
