package com.example.mywishlist.ui

import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.mywishlist.ui.screen.wishlist.WishlistScreen
import com.example.mywishlist.ui.theme.MyWishlistTheme

@Composable
fun WishlistApp() {
    MyWishlistTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WishlistScreen()
        }
    }
}
