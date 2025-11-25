package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.WishlistItem

@Composable
fun WishlistItemCard(
    item: WishlistItem,
    onTogglePurchased: (Boolean) -> Unit,
    onAddSavingsClick: () -> Unit
) {
    val progressPercent = (item.progress * 100).toInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F2FB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Left Text
                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A3DB5)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Target: Rp ${item.targetPrice}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6D6A7C)
                    )
                    Text(
                        text = "Terkumpul: Rp ${item.savedAmount}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF6750A4)
                    )
                }

                // Category Chip Modern
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            item.category.displayName,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFF6750A4),
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(50)
                )
            }

            // Progress Section
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                // Progress bar with rounded corners
                LinearProgressIndicator(
                    progress = item.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50)),
                    color = Color(0xFF6750A4),
                    trackColor = Color(0xFFE7E0F6)
                )

                Text(
                    text = "$progressPercent% tercapai",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4A3DB5)
                )
            }

            // Bottom Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                // Purchased Checkbox
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(
                        checked = item.isPurchased,
                        onCheckedChange = onTogglePurchased,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF6750A4)
                        )
                    )

                    Text(
                        text = if (item.isPurchased) "Sudah dibeli 🎉" else "Belum dibeli",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (item.isPurchased) Color(0xFF4A3DB5) else Color(0xFF6D6A7C)
                    )
                }

                // Add Savings Button
                Button(
                    onClick = onAddSavingsClick,
                    enabled = !item.isPurchased,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6750A4),
                        disabledContainerColor = Color(0xFFBDB3D6)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tambah tabungan", color = Color.White)
                }
            }
        }
    }
}