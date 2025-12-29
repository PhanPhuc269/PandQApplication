package com.group1.pandqapplication.admin

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.group1.pandqapplication.admin.ui.orders.AdminOrderManagementScreen
import com.group1.pandqapplication.admin.ui.dashboard.AdminDashboardScreen
import com.group1.pandqapplication.admin.ui.analytics.AdminAnalyticsScreen
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.group1.pandqapplication.admin.ui.analysis.SalesAnalysisScreen
import com.group1.pandqapplication.admin.ui.components.AdminDrawerContent
import com.group1.pandqapplication.admin.ui.navigation.AdminScreen
import com.group1.pandqapplication.admin.ui.promotions.AdminPromotionsScreen
import com.group1.pandqapplication.admin.ui.category.CategoryManagementScreen
import com.group1.pandqapplication.admin.ui.supplier.SupplierManagementScreen
import com.group1.pandqapplication.admin.ui.role.RoleManagementScreen
import com.group1.pandqapplication.admin.ui.branch.BranchManagementScreen
import com.group1.pandqapplication.admin.ui.inventory.InventoryScreen
import com.group1.pandqapplication.admin.ui.product.AddProductScreen
import com.group1.pandqapplication.admin.ui.orders.AdminOrderDetailsScreen
import com.group1.pandqapplication.admin.ui.promotions.CreatePromotionScreen
import com.group1.pandqapplication.admin.ui.profile.AdminProfileScreen
import com.group1.pandqapplication.admin.ui.setting.AdminSettingsScreen
import com.group1.pandqapplication.admin.ui.customer.CustomerListScreen
import com.group1.pandqapplication.admin.ui.shipping.ShippingManagementScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var currentRoute by remember { mutableStateOf(AdminScreen.Login.route) }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // Disable drawer when on login screen
                val isLoggedIn = currentRoute != AdminScreen.Login.route

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = isLoggedIn, // Disable swipe gesture on login screen
                    drawerContent = {
                        if (isLoggedIn) { // Only show drawer content when logged in
                            ModalDrawerSheet {
                                AdminDrawerContent(
                                    currentRoute = currentRoute,
                                    onNavigate = { route ->
                                        currentRoute = route
                                        navController.navigate(route) {
                                            popUpTo(AdminScreen.Dashboard.route) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch { drawerState.close() }
                                    },
                                    onLogout = { 
                                        // Navigate back to login
                                        currentRoute = AdminScreen.Login.route
                                        navController.navigate(AdminScreen.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) {
                    Scaffold(
                        // Bottom Bar removed
                    ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = AdminScreen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(AdminScreen.Dashboard.route) {
                            AdminDashboardScreen(
                                onMenuClick = { scope.launch { drawerState.open() } },
                                onNavigateToOrders = {
                                    currentRoute = AdminScreen.Orders.route
                                    navController.navigate(AdminScreen.Orders.route)
                                },
                                onNavigateToPromotions = {
                                    currentRoute = AdminScreen.Promotions.route
                                    navController.navigate(AdminScreen.Promotions.route)
                                },
                                onNavigateToAddProduct = {
                                    navController.navigate(AdminScreen.AddProduct.route)
                                },
                                onNavigateToProfile = {
                                    navController.navigate(AdminScreen.Profile.route)
                                },
                                onNavigateToInventory = { navController.navigate(AdminScreen.Inventory.route) },
                                onNavigateToCategory = { navController.navigate(AdminScreen.CategoryManagement.route) },
                                onNavigateToBranch = { navController.navigate(AdminScreen.BranchManagement.route) },
                                onNavigateToSupplier = { navController.navigate(AdminScreen.SupplierManagement.route) },
                                onNavigateToRoles = { navController.navigate(AdminScreen.RoleManagement.route) },
                                onNavigateToAnalytics = { navController.navigate(AdminScreen.Analytics.route) },
                                onNavigateToSettings = { navController.navigate(AdminScreen.Settings.route) },
                                onNavigateToCustomer = { navController.navigate(AdminScreen.CustomerList.route) },
                                onNavigateToShipping = { navController.navigate(AdminScreen.ShippingManagement.route) }
                            )
                        }
                        composable(AdminScreen.Orders.route) {
                            AdminOrderManagementScreen(
                                onNavigateToOrderDetails = { orderId ->
                                    navController.navigate(AdminScreen.OrderDetails.route) // In real app, pass ID
                                }
                            )
                        }
                        composable(AdminScreen.Promotions.route) {
                            AdminPromotionsScreen(
                                onNavigateToCreatePromotion = {
                                    navController.navigate(AdminScreen.CreatePromotion.route)
                                }
                            )
                        }
                        composable(AdminScreen.Analytics.route) {
                            AdminAnalyticsScreen(
                                onNavigateToSalesAnalysis = { navController.navigate(AdminScreen.SalesAnalysis.route) }
                            )
                        }
                        composable(AdminScreen.SalesAnalysis.route) {
                            SalesAnalysisScreen(onBackClick = { navController.popBackStack() })
                        }
                        
                        // Sub-screens
                        composable(AdminScreen.CategoryManagement.route) {
                            CategoryManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.SupplierManagement.route) {
                            SupplierManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.RoleManagement.route) {
                            RoleManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.BranchManagement.route) {
                            BranchManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.Inventory.route) {
                            InventoryScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToAddProduct = { navController.navigate(AdminScreen.AddProduct.route) }
                            )
                        }
                        composable(AdminScreen.AddProduct.route) {
                            AddProductScreen(
                                onBackClick = { navController.popBackStack() },
                                onCancelClick = { navController.popBackStack() }
                            )
                        }
                        composable(AdminScreen.Profile.route) {
                            AdminProfileScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToSettings = { navController.navigate(AdminScreen.Settings.route) }
                            )
                        }
                        composable(AdminScreen.Settings.route) {
                            AdminSettingsScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.CustomerList.route) {
                            CustomerListScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.ShippingManagement.route) {
                            ShippingManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.OrderDetails.route) {
                            AdminOrderDetailsScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.CreatePromotion.route) {
                            CreatePromotionScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.Login.route) {
                            com.group1.pandqapplication.admin.ui.login.AdminLoginScreen(
                                onLoginSuccess = {
                                    currentRoute = AdminScreen.Dashboard.route
                                    navController.navigate(AdminScreen.Dashboard.route) {
                                        popUpTo(AdminScreen.Login.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}
}
