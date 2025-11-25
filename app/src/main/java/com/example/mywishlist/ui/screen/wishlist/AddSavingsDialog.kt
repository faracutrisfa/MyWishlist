package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.WishlistItem

@Composable
fun AddSavingsDialog(
    item: WishlistItem,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val isValid = amountText.toLongOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF7F2FB))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Title
                Text(
                    text = "Tambah Tabungan",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A3DB5)
                    )
                )

                // Subtext
                Text(
                    text = "Untuk: ${item.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5A4EB9)
                )

                // Input Field
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                    label = { Text("Jumlah (Rp)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1C1B1F),               // teks saat fokus
                        unfocusedTextColor = Color(0xFF1C1B1F),             // teks saat tidak fokus
                        focusedBorderColor = Color(0xFF6750A4),
                        unfocusedBorderColor = Color(0xFFB1A7C9),
                        focusedLabelColor = Color(0xFF6750A4),
                        unfocusedLabelColor = Color(0xFF6D6A7C),
                        cursorColor = Color(0xFF6750A4)
                    )
                )

                // Buttons Row
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel Button
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6750A4)
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = SolidColor(Color(0xFF6750A4))
                        )
                    ) {
                        Text("Batal", color = Color(0xFF6750A4))
                    }

                    // Confirm Button
                    Button(
                        onClick = {
                            val amount = amountText.toLongOrNull() ?: 0L
                            onConfirm(amount)
                        },
                        enabled = isValid,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6750A4),
                            disabledContainerColor = Color(0xFFBDB3D6)
                        )
                    ) {
                        Text("Tambah", color = Color.White)
                    }
                }
            }
        }
    )
}
