package com.group1.pandqapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.group1.pandqapplication.MainViewModel
import com.group1.pandqapplication.ui.home.HomeScreen
import com.group1.pandqapplication.ui.login.LoginScreen
import com.group1.pandqapplication.ui.checkout.CheckoutScreen
import com.group1.pandqapplication.ui.ordertracking.OrderTrackingScreen
import com.group1.pandqapplication.ui.shipping.ShippingAddressScreen

@Composable
fun PandQNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Onboarding.route,
    destinationAfterSplash: String = Screen.Onboarding.route,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            com.group1.pandqapplication.ui.splash.SplashScreen(
                navController = navController,
                onInitializationComplete = {
                    navController.navigate(destinationAfterSplash) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Onboarding.route) {
            com.group1.pandqapplication.ui.onboarding.OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            com.group1.pandqapplication.ui.main.MainScreen(
                onLogout = {
                    mainViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onOrderClick = {
                    navController.navigate(Screen.OrderTracking.route)
                },
                onNavigateToOrder = { orderId ->
                    // TODO: Pass orderId to OrderTracking screen when it supports dynamic order
                    navController.navigate(Screen.OrderTracking.route)
                },
                onNavigateToProduct = { productId ->
                    // TODO: Pass productId to ProductDetail screen when it supports dynamic product
                    navController.navigate(Screen.ProductDetail.route)
                },
                onNavigateToPromotion = { promoId ->
                    // TODO: Navigate to promotion screen when available
                    // For now, stay on current screen
                }
            )
        }
        composable(Screen.Search.route) {
            com.group1.pandqapplication.ui.search.SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            com.group1.pandqapplication.ui.product.ProductDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        composable(Screen.Cart.route) {
            com.group1.pandqapplication.ui.cart.CartScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCheckoutClick = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditAddressClick = {
                    navController.navigate(Screen.ShippingAddress.route)
                }
            )
        }
        composable(Screen.OrderTracking.route) {
            OrderTrackingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.ShippingAddress.route) {
            ShippingAddressScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
