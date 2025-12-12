package com.group1.pandqapplication.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// import com.group1.pandqapplication.ui.common.AccountScreen
import com.group1.pandqapplication.ui.notification.NotificationsScreen
// import com.group1.pandqapplication.ui.common.OrdersScreen
import com.group1.pandqapplication.ui.components.BottomNavItem
import com.group1.pandqapplication.ui.components.BottomNavigationBar
import com.group1.pandqapplication.ui.home.HomeScreen
import com.group1.pandqapplication.ui.navigation.Screen
import com.group1.pandqapplication.ui.order.OrdersScreen
import com.group1.pandqapplication.ui.product.ProductDetailScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    onProductClick: () -> Unit,
    onSearchClick: () -> Unit,
    onOrderClick: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        onLogout = onLogout,
                        onProductClick = onProductClick,
                        onSearchClick = onSearchClick
                    )
                }
                composable(BottomNavItem.Orders.route) { 
                    OrdersScreen(
                        onOrderClick = onOrderClick
                    ) 
                }
                composable(BottomNavItem.Notifications.route) { NotificationsScreen() }
                composable(BottomNavItem.Account.route) { 
                    com.group1.pandqapplication.ui.account.AccountScreen(
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}
