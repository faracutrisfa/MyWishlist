package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.ItemCategory

@Composable
fun CategoryFilterRow(
    selectedCategory: ItemCategory?,
    onCategorySelected: (ItemCategory?) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF7F2FB),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = {
                Text(
                    "Semua",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = if (selectedCategory == null) FontWeight.SemiBold else FontWeight.Normal
                    )
                )
            },
            shape = RoundedCornerShape(999.dp),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0xFFE7E0F6),
                labelColor = Color(0xFF49454F),
                selectedContainerColor = Color(0xFF6750A4),
                selectedLabelColor = Color.White
            )
        )

        ItemCategory.values().forEach { category ->
            val isSelected = selectedCategory == category

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        category.displayName,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    )
                },
                shape = RoundedCornerShape(999.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFFE7E0F6),
                    labelColor = Color(0xFF49454F),
                    selectedContainerColor = Color(0xFF6750A4),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}
