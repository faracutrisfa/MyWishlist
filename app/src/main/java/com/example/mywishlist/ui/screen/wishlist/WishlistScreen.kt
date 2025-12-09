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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.local.WishlistDatabase
import com.example.mywishlist.data.model.ItemCategory
import com.example.mywishlist.data.model.WishlistItem
import com.example.mywishlist.ui.components.EmptyState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val TextPrimary = Color(0xFF241E49)
private val TextSecondary = Color(0xFF6D6A7C)

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
        val base = if (selectedCategory == null) items else items.filter { it.category == selectedCategory }
        when (sortOption) {
            SortOption.NAME -> base.sortedBy { it.name.lowercase() }
            SortOption.PROGRESS -> base.sortedByDescending { it.progress }
            SortOption.TARGET_LOW -> base.sortedBy { it.targetPrice }
            SortOption.REMAINING_LOW -> base.sortedBy { it.targetPrice - it.savedAmount }
        }
    }

    val totalTarget = remember(items) { items.sumOf { it.targetPrice } }
    val totalSaved = remember(items) { items.sumOf { it.savedAmount } }
    val overallProgress = if (totalTarget > 0) totalSaved.toFloat() / totalTarget.toFloat() else 0f

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "MyWishlist",
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
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White,
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah item")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F3FF))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color(0x22000000),
                                spotColor = Color(0x22000000)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF6750A4)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        listOf(
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
                                text = "Halo 👋",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color(0xFFEADDFF)
                                )
                            )
                            Text(
                                text = "Capai wishlist kamu pelan-pelan",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Terkumpul",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFEADDFF)
                                        )
                                    )
                                    Text(
                                        text = "Rp $totalSaved",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Target",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFEADDFF)
                                        )
                                    )
                                    Text(
                                        text = if (totalTarget == 0L) "—" else "Rp $totalTarget",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = overallProgress,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(999.dp)),
                                color = Color(0xFFFFD8E4),
                                trackColor = Color.White.copy(alpha = 0.16f)
                            )
                        }
                    }
                }

                item {
                    CategoryFilterRow(
                        modifier = Modifier.fillMaxWidth(),
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daftar wishlist",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )
                        Box {
                            FilledTonalButton(
                                onClick = { sortExpanded = true },
                                shape = RoundedCornerShape(999.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color(0xFFF3EDF7),
                                    contentColor = Color(0xFF4A3DB5)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = sortOption.label,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            DropdownMenu(
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false },
                                containerColor = Color.White,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                SortOption.values().forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.label, color = TextPrimary) },
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
                }

                if (filteredItems.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyState()
                        }
                    }
                } else {
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

    deleteTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            title = {
                Text(
                    "Hapus Wishlist",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
            },
            text = {
                Text(
                    "Yakin ingin menghapus \"${target.name}\" dari wishlist?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )
            },
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
                    Text("Batal", color = TextPrimary)
                }
            },
            containerColor = Color.White,
            titleContentColor = TextPrimary,
            textContentColor = TextSecondary
        )
    }
}
