package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.WishlistItem

@Composable
fun WishlistItemCard(
    item: WishlistItem,
    onTogglePurchased: (Boolean) -> Unit,
    onAddSavingsClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val progressPercent = (item.progress * 100).toInt()
    val remaining = (item.targetPrice - item.savedAmount).coerceAtLeast(0L)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 6.dp
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = Brush.verticalGradient(
                listOf(
                    Color(0xFFE7E0F6),
                    Color(0xFFD3CBEF)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF241E49)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (item.isPurchased) "Goal tercapai 🎉" else "Dalam proses nabung",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (item.isPurchased) Color(0xFF4A3DB5) else Color(0xFF6D6A7C)
                    )
                }

                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            item.category.displayName,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFF6750A4),
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(999.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoPill(
                    title = "Target",
                    value = "Rp ${item.targetPrice}",
                    modifier = Modifier.weight(1f)
                )
                InfoPill(
                    title = "Terkumpul",
                    value = "Rp ${item.savedAmount}",
                    modifier = Modifier.weight(1f)
                )
                InfoPill(
                    title = "Sisa",
                    value = "Rp $remaining",
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color(0xFFE7E0F6))
                ) {
                    LinearProgressIndicator(
                        progress = item.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        color = Color(0xFF6750A4),
                        trackColor = Color.Transparent
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$progressPercent% tercapai",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A3DB5)
                    )
                    Text(
                        text = if (item.isPurchased) "Goal selesai" else "Keep going ✨",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6D6A7C)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
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

                    Button(
                        onClick = onAddSavingsClick,
                        enabled = !item.isPurchased,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6750A4),
                            disabledContainerColor = Color(0xFFBDB3D6)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Tambah tabungan", color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDeleteClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xFFB3261E)
                        )
                    ) {
                        Text(
                            text = "Hapus",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoPill(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF8A8699)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF241E49)
        )
    }
}