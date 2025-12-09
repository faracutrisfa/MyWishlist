package com.example.mywishlist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Belum ada wishlist 🥺",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF241E49)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tambah barang impianmu.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color(0xFF6D6A7C)
            )
        )
    }
}
