package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.ItemCategory
import com.example.mywishlist.data.model.WishlistItem
import com.example.mywishlist.data.local.WishlistDatabase
import com.example.mywishlist.ui.components.EmptyState
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen() {
    val context = LocalContext.current
    val db = remember { WishlistDatabase.getInstance(context) }
    val dao = remember { db.wishlistDao() }
    val scope = rememberCoroutineScope()

    var items by remember { mutableStateOf<List<WishlistItem>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf<ItemCategory?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var savingsDialogFor by remember { mutableStateOf<WishlistItem?>(null) }

    // collect dari Room
    LaunchedEffect(Unit) {
        dao.getAllItems().collectLatest { list ->
            items = list
        }
    }

    val filteredItems = remember(items, selectedCategory) {
        if (selectedCategory == null) items else items.filter { it.category == selectedCategory }
    }

    Scaffold(
        containerColor = Color(0xFFF7F2FB), // Background lembut
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = Color(0xFF6750A4),
                tonalElevation = 8.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            "Wishlist Nabung",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6750A4),
                        titleContentColor = Color.White
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(14.dp),
                containerColor = Color(0xFF6750A4),
                contentColor = Color.White
            ) {
                Text(
                    "+",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // Filter kategori
            CategoryFilterRow(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (filteredItems.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        WishlistItemCard(
                            item = item,
                            onTogglePurchased = { isPurchased ->
                                scope.launch {
                                    dao.updateItem(item.copy(isPurchased = isPurchased))
                                }
                            },
                            onAddSavingsClick = { savingsDialogFor = item }
                        )
                    }
                }
            }
        }
    }

    // Tambah Item
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

    // Tambah Tabungan
    savingsDialogFor?.let { targetItem ->
        AddSavingsDialog(
            item = targetItem,
            onDismiss = { savingsDialogFor = null },
            onConfirm = { extraSavings ->
                scope.launch {
                    val newAmount = targetItem.savedAmount + extraSavings
                    dao.updateItem(targetItem.copy(savedAmount = newAmount))
                }
                savingsDialogFor = null
            }
        )
    }
}
