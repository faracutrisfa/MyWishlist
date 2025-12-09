package com.example.mywishlist.ui.screen.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, amount: Long) -> Unit
) {
    var name = remember { mutableStateOf("") }
    var amountText = remember { mutableStateOf("") }

    val isValid = name.value.isNotBlank() && amountText.value.toLongOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tambah Pengeluaran",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Nama pengeluaran") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = amountText.value,
                    onValueChange = { amountText.value = it.filter { c -> c.isDigit() } },
                    label = { Text("Jumlah (Rp)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    val amount = amountText.value.toLongOrNull() ?: 0L
                    onConfirm(name.value, amount)
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
