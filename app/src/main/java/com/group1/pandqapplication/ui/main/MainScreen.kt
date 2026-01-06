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
    onProductClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit = {},
    onOrderClick: (String) -> Unit, // orderId parameter
    onPersonalInfoClick: () -> Unit = {},
    onAddressListClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onPolicyClick: () -> Unit = {},
    onUserGuideClick: () -> Unit = {}, // Added param
    onNavigateToOrder: (String?) -> Unit = { orderId -> orderId?.let { onOrderClick(it) } }, // Navigate to order with orderId
    onNavigateToProduct: (String?) -> Unit = { id -> id?.let { onProductClick(it) } }, // Navigate to product with optional productId
    onNavigateToPromotion: (String?) -> Unit = { _ -> } // Navigate to promotion
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(
                        onLogout = onLogout,
                        onProductClick = onProductClick,
                        onSearchClick = onSearchClick,
                        onCategoryClick = onCategoryClick,
                        onCartClick = onCartClick
                    )
                }
                composable(BottomNavItem.Orders.route) { 
                    OrdersScreen(
                        onOrderClick = { orderId -> onNavigateToOrder(orderId) }
                    ) 
                }
                composable(BottomNavItem.Notifications.route) { 
                    NotificationsScreen(
                        onNotificationClick = { targetUrl ->
                            // Parse targetUrl and navigate to appropriate screen
                            when {
                                targetUrl == null -> { /* No navigation */ }
                                targetUrl.contains("home") -> {
                                    // Navigate to home tab
                                    navController.navigate(BottomNavItem.Home.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                                targetUrl.contains("orders") -> {
                                    // Extract orderId from URL like "pandq://orders/29381"
                                    val orderId = targetUrl.substringAfterLast("/")
                                    onNavigateToOrder(orderId)
                                }
                                targetUrl.contains("products") -> {
                                    val productId = targetUrl.substringAfterLast("/")
                                    onNavigateToProduct(productId)
                                }
                                targetUrl.contains("promotions") || targetUrl.contains("coupons") || targetUrl.contains("flash-sale") -> {
                                    val promoId = targetUrl.substringAfterLast("/")
                                    onNavigateToPromotion(promoId)
                                }
                            }
                        }
                    )
                }
                composable(BottomNavItem.Account.route) { 
                    com.group1.pandqapplication.ui.account.AccountScreen(
                        onLogout = onLogout,
                        onNavigateToPersonalInfo = onPersonalInfoClick,
                        onNavigateToAddressList = onAddressListClick,
                        onNavigateToSupport = onSupportClick,
                        onNavigateToUserGuide = onUserGuideClick,
                        onNavigateToPolicy = onPolicyClick
                    )
                }
            }
        }
    }
}

