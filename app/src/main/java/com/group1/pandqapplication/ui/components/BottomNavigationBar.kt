package com.group1.pandqapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.group1.pandqapplication.R
import com.group1.pandqapplication.ui.navigation.Screen

sealed class BottomNavItem(val route: String, val icon: ImageVector, val labelResId: Int) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Filled.Home, R.string.nav_home)
    object Orders : BottomNavItem("orders", Icons.Filled.ReceiptLong, R.string.nav_orders)
    object Notifications : BottomNavItem("notifications", Icons.Filled.Notifications, R.string.nav_notifications)
    object Account : BottomNavItem("account", Icons.Filled.Person, R.string.nav_account)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Orders,
        BottomNavItem.Notifications,
        BottomNavItem.Account
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFFec3713)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val label = stringResource(item.labelResId)
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = label) },
                label = { Text(label) },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFec3713),
                    selectedTextColor = Color(0xFFec3713),
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
