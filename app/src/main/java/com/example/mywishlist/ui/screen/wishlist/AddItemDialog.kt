package com.example.mywishlist.ui.screen.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mywishlist.data.model.ItemCategory
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.SolidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onSave: (String, Long, ItemCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ItemCategory.ELECTRONICS) }
    var expanded by remember { mutableStateOf(false) }

    val isValid = name.isNotBlank() && priceText.toLongOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = null,
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF7F2FB))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    "Tambah Wishlist",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A3DB5)
                    )
                )

                // Input Nama
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama barang") },
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

                // Input Harga
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it.filter { c -> c.isDigit() } },
                    label = { Text("Target harga (Rp)") },
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

                // Dropdown kategori
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = category.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
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

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        ItemCategory.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    category = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Buttons
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel button
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


                    // Save button
                    Button(
                        onClick = {
                            val price = priceText.toLongOrNull() ?: 0L
                            onSave(name, price, category)
                        },
                        enabled = isValid,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6750A4),
                            disabledContainerColor = Color(0xFFBDB3D6)
                        )
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }
            }
        }
    )
}