package com.example.mywishlist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mywishlist.ui.screen.budget.BudgetScreen
import com.example.mywishlist.ui.screen.wishlist.WishlistScreen
import com.example.mywishlist.ui.theme.MyWishlistTheme

private enum class HomeTab {
    WISHLIST,
    BUDGET
}

@Composable
fun WishlistApp() {
    MyWishlistTheme {
        var currentTab by remember { mutableStateOf(HomeTab.WISHLIST) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F3FF)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFF5F3FF),
                                Color(0xFFFDF8FF),
                                Color(0xFFFFF9FB)
                            )
                        )
                    )
            ) {
                Scaffold(
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets.systemBars,
                    bottomBar = {
                        ModernBottomBar(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = 0.dp,
                                end = 0.dp,
                                top = innerPadding.calculateTopPadding(),
                                bottom = innerPadding.calculateBottomPadding()
                            )
                    ) {
                        when (currentTab) {
                            HomeTab.WISHLIST -> WishlistScreen()
                            HomeTab.BUDGET -> BudgetScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernBottomBar(
    currentTab: HomeTab,
    onTabSelected: (HomeTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color(0x33000000),
                    spotColor = Color(0x33000000)
                ),
            containerColor = Color.White.copy(alpha = 0.94f),
            tonalElevation = 0.dp
        ) {

            NavigationBarItem(
                selected = currentTab == HomeTab.WISHLIST,
                onClick = { onTabSelected(HomeTab.WISHLIST) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist"
                    )
                },
                label = {
                    Text(
                        "Wishlist",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6750A4),
                    unselectedIconColor = Color(0xFF9C92B5),
                    indicatorColor = Color(0xFFEADDFF),
                    selectedTextColor = Color(0xFF4A3DB5),
                    unselectedTextColor = Color(0xFF9C92B5)
                )
            )

            NavigationBarItem(
                selected = currentTab == HomeTab.BUDGET,
                onClick = { onTabSelected(HomeTab.BUDGET) },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.AccountBalanceWallet,
                        contentDescription = "Budget"
                    )
                },
                label = {
                    Text(
                        "Budget",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6750A4),
                    unselectedIconColor = Color(0xFF9C92B5),
                    indicatorColor = Color(0xFFEADDFF),
                    selectedTextColor = Color(0xFF4A3DB5),
                    unselectedTextColor = Color(0xFF9C92B5)
                )
            )
        }
    }
}
