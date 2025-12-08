package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.example.mywishlist.data.local.WishlistDatabase
import com.example.mywishlist.data.model.ItemCategory
import com.example.mywishlist.data.model.WishlistItem
import com.example.mywishlist.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen() {

    val context = LocalContext.current
    val db = remember { WishlistDatabase.getInstance(context) }
    val dao = remember { db.wishlistDao() }
    val scope = rememberCoroutineScope()

    var items by remember { mutableStateOf<List<WishlistItem>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }
    var sortOption by remember { mutableStateOf(SortOption.NAME) }
    var sortExpanded by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var savingsDialogFor by remember { mutableStateOf<WishlistItem?>(null) }
    var deleteTarget by remember { mutableStateOf<WishlistItem?>(null) }

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        dao.getAllItems().collectLatest { list ->
            items = list
        }
    }

    val filteredItems = remember(items, selectedCategory, sortOption) {
        val base = if (selectedCategory == null) items
        else items.filter { it.category == selectedCategory }

        when (sortOption) {
            SortOption.NAME -> base.sortedBy { it.name.lowercase() }
            SortOption.PROGRESS -> base.sortedByDescending { it.progress }
            SortOption.TARGET_LOW -> base.sortedBy { it.targetPrice }
            SortOption.REMAINING_LOW -> base.sortedBy { it.targetPrice - it.savedAmount }
        }
    }

    val totalTarget = remember(items) { items.sumOf { it.targetPrice } }
    val totalSaved = remember(items) { items.sumOf { it.savedAmount } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MyWishlist",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6750A4),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah item")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F3FF)) // soft ungu background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // SUMMARY
                SummaryHeader(totalTarget = totalTarget, totalSaved = totalSaved)

                // FILTER KATEGORI
                CategoryFilterRow(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                // SORT AREA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar wishlist",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Box {
                        OutlinedButton(
                            onClick = { sortExpanded = true },
                            shape = RoundedCornerShape(999.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFF3EDF7)
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = Color(0xFF4A3DB5)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = sortOption.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF4A3DB5)
                            )
                        }

                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false },
                            containerColor = Color.White
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label) },
                                    onClick = {
                                        sortExpanded = false
                                        sortOption = option
                                        scope.launch {
                                            listState.animateScrollToItem(0)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // LIST
                if (filteredItems.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredItems, key = { it.id }) { item ->
                            WishlistItemCard(
                                item = item,
                                onTogglePurchased = { isPurchased ->
                                    scope.launch {
                                        dao.updateItem(item.copy(isPurchased = isPurchased))
                                    }
                                },
                                onAddSavingsClick = { savingsDialogFor = item },
                                onDeleteClick = { deleteTarget = item }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog – tambah item
    if (showAddDialog) {
        AddItemDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, price, category ->
                scope.launch {
                    dao.insertItem(
                        WishlistItem(
                            name = name,
                            targetPrice = price,
                            category = category
                        )
                    )
                }
                showAddDialog = false
            }
        )
    }

    // Dialog – tambah tabungan
    savingsDialogFor?.let { item ->
        AddSavingsDialog(
            item = item,
            onDismiss = { savingsDialogFor = null },
            onConfirm = { extra ->
                scope.launch {
                    dao.updateItem(item.copy(savedAmount = item.savedAmount + extra))
                }
                savingsDialogFor = null
            }
        )
    }

    // Dialog – hapus
    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = { Text("Hapus Wishlist") },
            text = { Text("Yakin ingin menghapus \"${target.name}\" dari wishlist?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch { dao.deleteItem(target) }
                        deleteTarget = null
                    }
                ) {
                    Text("Hapus", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun SummaryHeader(
    totalTarget: Long,
    totalSaved: Long
) {
    val remaining = (totalTarget - totalSaved).coerceAtLeast(0L)
    val progress = if (totalTarget > 0) totalSaved.toFloat() / totalTarget.toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6750A4)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6750A4),
                            Color(0xFF4A3DB5)
                        )
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Ringkasan tabungan",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "Terkumpul: Rp $totalSaved",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFEADDFF)
                )
            )
            Text(
                text = "Total target: Rp $totalTarget",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFEADDFF)
                )
            )
            Text(
                text = "Sisa: Rp $remaining",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFF5F5FF)
                )
            )

            Spacer(Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = Color(0xFFFFD8E4),
                trackColor = Color(0xFF4A3DB5)
            )
        }
    }
}
