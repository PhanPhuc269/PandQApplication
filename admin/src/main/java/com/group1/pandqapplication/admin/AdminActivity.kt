package com.group1.pandqapplication.admin

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.group1.pandqapplication.admin.ui.orders.AdminOrderManagementScreen
import com.group1.pandqapplication.admin.ui.dashboard.AdminDashboardScreen
import com.group1.pandqapplication.admin.ui.analytics.AdminAnalyticsScreen
import com.group1.pandqapplication.admin.ui.analytics.AnalyticsDetailScreen
import com.group1.pandqapplication.admin.ui.analytics.DailyAnalyticsDetailScreen
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.navigation
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.AdminUserManager

import com.group1.pandqapplication.admin.ui.components.AdminDrawerContent
import com.group1.pandqapplication.admin.ui.navigation.AdminScreen
import com.group1.pandqapplication.admin.ui.promotions.AdminPromotionsScreen
import com.group1.pandqapplication.admin.ui.category.CategoryManagementScreen
import com.group1.pandqapplication.admin.ui.supplier.SupplierManagementScreen
import com.group1.pandqapplication.admin.ui.role.RoleManagementScreen
import com.group1.pandqapplication.admin.ui.branch.BranchManagementScreen
import com.group1.pandqapplication.admin.ui.inventory.InventoryScreen
import com.group1.pandqapplication.admin.ui.product.AddProductScreen
import com.group1.pandqapplication.admin.ui.product.ProductManagementScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType

import com.group1.pandqapplication.admin.ui.orders.AdminOrderDetailsScreen
import com.group1.pandqapplication.admin.ui.promotions.CreatePromotionScreen
import com.group1.pandqapplication.admin.ui.profile.AdminProfileScreen
import com.group1.pandqapplication.admin.ui.setting.AdminSettingsScreen
import com.group1.pandqapplication.admin.ui.customer.CustomerListScreen
import com.group1.pandqapplication.admin.ui.customer.CustomerDetailScreen
import com.group1.pandqapplication.admin.ui.shipping.ShippingManagementScreen
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminActivity : FragmentActivity() {
    
    @Inject
    lateinit var adminUserManager: AdminUserManager
    
    @Inject
    lateinit var adminSettingsManager: com.group1.pandqapplication.admin.data.AdminSettingsManager
    
    private var isAppInBackground = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                var currentRoute by remember { mutableStateOf(AdminScreen.Login.route) }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                
                // Collect user data from AdminUserManager
                val currentUser by adminUserManager.currentUser.collectAsState()
                val userName = currentUser?.fullName ?: "Admin"
                val userRole = currentUser?.role ?: "Administrator"
                val avatarUrl = currentUser?.avatarUrl

                // Disable drawer when on login screen
                val isLoggedIn = currentRoute != AdminScreen.Login.route
                
                // Load user data when logged in
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn && currentUser == null) {
                        adminUserManager.loadCurrentUser()
                    }
                }
                
                // Collect settings
                val isReturnScreenEnabled by adminSettingsManager.isReturnScreenEnabled.collectAsState()
                
                // Track if we need to show return screen after resume
                var shouldShowReturnScreen by remember { mutableStateOf(false) }
                
                // Use rememberUpdatedState to avoid stale closures in the lifecycle callback
                val currentIsLoggedIn by androidx.compose.runtime.rememberUpdatedState(isLoggedIn)
                val currentIsReturnScreenEnabled by androidx.compose.runtime.rememberUpdatedState(isReturnScreenEnabled)
                
                // Lifecycle observer to detect app going to background and resuming
                // Use ProcessLifecycleOwner to detect app-level lifecycle, not just composable lifecycle
                androidx.compose.runtime.DisposableEffect(Unit) {
                    val processLifecycleOwner = androidx.lifecycle.ProcessLifecycleOwner.get()
                    val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
                        android.util.Log.d("ReturnScreen", "Lifecycle event: $event, isLoggedIn=$currentIsLoggedIn, isReturnScreenEnabled=$currentIsReturnScreenEnabled")
                        when (event) {
                            androidx.lifecycle.Lifecycle.Event.ON_STOP -> {
                                // Only track if logged in and return screen enabled
                                if (currentIsLoggedIn && currentIsReturnScreenEnabled) {
                                    isAppInBackground = true
                                    android.util.Log.d("ReturnScreen", "App went to background, isAppInBackground=true")
                                }
                            }
                            androidx.lifecycle.Lifecycle.Event.ON_START -> {
                                android.util.Log.d("ReturnScreen", "ON_START: isAppInBackground=$isAppInBackground")
                                if (isAppInBackground && currentIsLoggedIn && currentIsReturnScreenEnabled) {
                                    shouldShowReturnScreen = true
                                    isAppInBackground = false
                                    android.util.Log.d("ReturnScreen", "Set shouldShowReturnScreen=true")
                                }
                            }
                            else -> {}
                        }
                    }
                    processLifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        processLifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }
                
                // Navigate to Login Screen when app resumes from background (shows "Chào mừng trở lại" if session exists)
                LaunchedEffect(shouldShowReturnScreen) {
                    if (shouldShowReturnScreen && currentRoute != AdminScreen.Login.route) {
                        android.util.Log.d("ReturnScreen", "Navigating to Login screen for return flow")
                        currentRoute = AdminScreen.Login.route
                        navController.navigate(AdminScreen.Login.route)
                        shouldShowReturnScreen = false
                    }
                }

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
                                        // Clear user data and navigate back to login
                                        adminUserManager.clearUser()
                                        currentRoute = AdminScreen.Login.route
                                        navController.navigate(AdminScreen.Login.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    },
                                    userName = userName,
                                    userRole = userRole,
                                    avatarUrl = avatarUrl
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
                        startDestination = AdminScreen.Login.route
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
                                    navController.navigate("add_product")
                                },


                                onNavigateToProfile = {
                                    navController.navigate(AdminScreen.Profile.route)
                                },
                                onNavigateToInventory = { navController.navigate("inventory_stats") },
                                onNavigateToCategory = { navController.navigate(AdminScreen.CategoryManagement.route) },
                                onNavigateToBranch = { navController.navigate(AdminScreen.BranchManagement.route) },
                                onNavigateToSupplier = { navController.navigate(AdminScreen.SupplierManagement.route) },
                                onNavigateToRoles = { navController.navigate(AdminScreen.RoleManagement.route) },
                                onNavigateToAnalytics = { navController.navigate(AdminScreen.Analytics.route) },
                                onNavigateToSettings = { navController.navigate(AdminScreen.Settings.route) },
                                onNavigateToCustomer = { navController.navigate(AdminScreen.CustomerList.route) },
                                onNavigateToShipping = { navController.navigate(AdminScreen.ShippingManagement.route) },
                                onNavigateToNotifications = { navController.navigate(AdminScreen.NotificationList.route) },
                                userName = userName,
                                avatarUrl = avatarUrl
                            )
                        }
                        composable(AdminScreen.Orders.route) {
                            AdminOrderManagementScreen(
                                onNavigateToOrderDetails = { orderId ->
                                    navController.navigate(AdminScreen.OrderDetails.route) // In real app, pass ID
                                }
                            )
                        }
                        
                        // Promotion Management Graph (Shared ViewModel Scope)
                        navigation(
                            startDestination = AdminScreen.Promotions.route,
                            route = "promotion_graph"
                        ) {
                            composable(AdminScreen.Promotions.route) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry("promotion_graph")
                                }
                                val viewModel: com.group1.pandqapplication.admin.ui.promotions.PromotionViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                
                                AdminPromotionsScreen(
                                    onNavigateToCreatePromotion = {
                                        navController.navigate(AdminScreen.CreatePromotion.route)
                                    },
                                    onNavigateToEditPromotion = { promotionId ->
                                        navController.navigate("edit_promotion/$promotionId")
                                    },
                                    onBackClick = {
                                        navController.navigate(AdminScreen.Dashboard.route) {
                                            popUpTo(AdminScreen.Dashboard.route) { inclusive = true }
                                        }
                                    },
                                    viewModel = viewModel
                                )
                            }
                            composable(AdminScreen.CreatePromotion.route) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry("promotion_graph")
                                }
                                val viewModel: com.group1.pandqapplication.admin.ui.promotions.PromotionViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                
                                CreatePromotionScreen(
                                    promotionId = null,
                                    onBackClick = { navController.popBackStack() },
                                    viewModel = viewModel
                                )
                            }
                            composable(
                                route = "edit_promotion/{promotionId}",
                                arguments = listOf(
                                    androidx.navigation.navArgument("promotionId") { type = androidx.navigation.NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry("promotion_graph")
                                }
                                val viewModel: com.group1.pandqapplication.admin.ui.promotions.PromotionViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                val promotionId = backStackEntry.arguments?.getString("promotionId")
                                
                                CreatePromotionScreen(
                                    promotionId = promotionId,
                                    onBackClick = { navController.popBackStack() },
                                    viewModel = viewModel
                                )
                            }
                        }
                        composable(AdminScreen.Analytics.route) {
                            AdminAnalyticsScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToDetail = { reportType, range -> 
                                    navController.navigate("analytics_detail?reportType=$reportType&range=$range") 
                                },
                                onNavigateToDailyDetail = { date ->
                                    navController.navigate("daily_analytics_detail/$date")
                                }
                            )
                        }
                        composable(
                            route = AdminScreen.AnalyticsDetail.route,
                            arguments = listOf(
                                navArgument("reportType") { type = NavType.StringType },
                                navArgument("range") { type = NavType.StringType; defaultValue = "30d" }
                            )
                        ) {
                            AnalyticsDetailScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = AdminScreen.DailyAnalyticsDetail.route,
                            arguments = listOf(
                                navArgument("date") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val date = backStackEntry.arguments?.getString("date") ?: ""
                            DailyAnalyticsDetailScreen(
                                date = date,
                                onBackClick = { navController.popBackStack() }
                            )
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
                        
                        // Inventory Statistics Screen
                        composable("inventory_stats") {
                            InventoryScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToAddProduct = { navController.navigate("add_product") },
                                onNavigateToEditProduct = { productId ->
                                    navController.navigate("add_product?productId=$productId")
                                }
                            )
                        }

                        // Product Management Graph (Shared ViewModel Scope)
                        navigation(
                            startDestination = AdminScreen.Inventory.route,
                            route = "product_graph"
                        ) {
                            composable(AdminScreen.Inventory.route) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry("product_graph")
                                }
                                val viewModel: com.group1.pandqapplication.admin.ui.product.AdminProductViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)
                                
                                ProductManagementScreen(
                                    viewModel = viewModel,
                                    onBackClick = { navController.popBackStack() },
                                    onAddProductClick = { navController.navigate("add_product") },
                                    onProductClick = { productId ->
                                        navController.navigate("add_product?productId=$productId")
                                    }
                                )
                            }
                            
                            composable(
                                route = AdminScreen.AddProduct.route,
                                arguments = listOf(navArgument("productId") { nullable = true; type = NavType.StringType })
                            ) { backStackEntry ->
                                val parentEntry = remember(backStackEntry) {
                                    navController.getBackStackEntry("product_graph")
                                }
                                val viewModel: com.group1.pandqapplication.admin.ui.product.AdminProductViewModel = androidx.hilt.navigation.compose.hiltViewModel(parentEntry)

                                val productId = backStackEntry.arguments?.getString("productId")
                                AddProductScreen(
                                    productId = productId,
                                    viewModel = viewModel,
                                    onBackClick = { navController.popBackStack() },
                                    onCancelClick = { navController.popBackStack() }
                                )
                            }
                        }

                        composable(AdminScreen.Profile.route) {
                            AdminProfileScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToSettings = { navController.navigate(AdminScreen.Settings.route) },
                                onChangePassword = { navController.navigate(AdminScreen.ChangePassword.route) },
                                onLogout = {
                                    adminUserManager.clearUser()
                                    currentRoute = AdminScreen.Login.route
                                    navController.navigate(AdminScreen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(AdminScreen.Settings.route) {
                            AdminSettingsScreen(
                                onBackClick = { navController.popBackStack() },
                                onEditProfile = { navController.navigate(AdminScreen.Profile.route) },
                                onChangePassword = { navController.navigate(AdminScreen.ChangePassword.route) },
                                userName = userName,
                                userRole = userRole,
                                avatarUrl = avatarUrl,
                                onLogout = {
                                    adminUserManager.clearUser()
                                    currentRoute = AdminScreen.Login.route
                                    navController.navigate(AdminScreen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(AdminScreen.CustomerList.route) {
                            CustomerListScreen(
                                onBackClick = { navController.popBackStack() },
                                onCustomerClick = { customerId ->
                                    navController.navigate("customer_detail/$customerId")
                                }
                            )
                        }
                        composable(
                            route = AdminScreen.CustomerDetail.route,
                            arguments = listOf(androidx.navigation.navArgument("customerId") { type = androidx.navigation.NavType.StringType })
                        ) {
                            CustomerDetailScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(AdminScreen.ShippingManagement.route) {
                            ShippingManagementScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.OrderDetails.route) {
                            AdminOrderDetailsScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(AdminScreen.Login.route) {
                            com.group1.pandqapplication.admin.ui.login.AdminLoginScreen(
                                onLoginSuccess = {
                                    currentRoute = AdminScreen.Dashboard.route
                                    navController.navigate(AdminScreen.Dashboard.route) {
                                        popUpTo(AdminScreen.Login.route) { inclusive = true }
                                    }
                                    // Load user data after login
                                    scope.launch {
                                        adminUserManager.loadCurrentUser(forceRefresh = true)
                                    }
                                }
                            )
                        }
                        composable(AdminScreen.ChangePassword.route) {
                            com.group1.pandqapplication.admin.ui.profile.ChangePasswordScreen(
                                onBackClick = { navController.popBackStack() },
                                onSuccess = { navController.popBackStack() }
                            )
                        }
                        composable(AdminScreen.NotificationTemplates.route) {
                            com.group1.pandqapplication.admin.ui.notification.NotificationTemplateScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(AdminScreen.NotificationList.route) {
                            com.group1.pandqapplication.admin.ui.notifications.AdminNotificationScreen()
                        }
                        
                        // Chat routes
                        composable(AdminScreen.Chats.route) {
                            com.group1.pandqapplication.admin.ui.chat.AdminChatsListScreen(
                                onChatSelected = { chatId ->
                                    navController.navigate("chat_details/$chatId")
                                }
                            )
                        }
                        composable(
                            route = AdminScreen.ChatDetails.route,
                            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                            com.group1.pandqapplication.admin.ui.chat.AdminChatScreen(
                                chatId = chatId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                    }
                }
            }
        }
        }
}

}
