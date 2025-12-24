package com.group1.pandqapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.group1.pandqapplication.MainViewModel
import com.group1.pandqapplication.shared.data.remote.dto.OrderHistoryDto
import com.group1.pandqapplication.ui.home.HomeScreen
import com.group1.pandqapplication.ui.login.LoginScreen
import com.group1.pandqapplication.ui.checkout.CheckoutScreen
import com.group1.pandqapplication.ui.ordertracking.OrderDetailScreen
import com.group1.pandqapplication.ui.ordertracking.OrderTrackingScreen
import com.group1.pandqapplication.ui.shipping.ShippingAddressScreen
import com.group1.pandqapplication.ui.support.SupportScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
                onProductClick = {
                    navController.navigate(Screen.ProductDetail.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onOrderClick = { order ->
                    val json = Gson().toJson(order)
                    val encoded = java.net.URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                    navController.navigate("${Screen.OrderTracking.route}/$encoded")
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
        composable(Screen.ProductDetail.route) {
            com.group1.pandqapplication.ui.product.ProductDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
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
        composable("${Screen.OrderTracking.route}/{orderJson}") { backStackEntry ->
            val orderJson = backStackEntry.arguments?.getString("orderJson")
            val order = remember(orderJson) {
                orderJson?.let {
                    val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Gson().fromJson(decoded, OrderHistoryDto::class.java)
                }
            }
            
            OrderTrackingScreen(
                order = order,
                onBackClick = {
                    navController.popBackStack()
                },
                onDetailClick = {
                    // Navigate to OrderDetail with order data
                    order?.let {
                        val json = Gson().toJson(it)
                        val encoded = java.net.URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
                        navController.navigate("${Screen.OrderDetail.route}/$encoded")
                    }
                },
                onSupportClick = {
                    navController.navigate(Screen.Support.route)
                }
            )
        }
        composable("${Screen.OrderDetail.route}/{orderJson}") { backStackEntry ->
            val orderJson = backStackEntry.arguments?.getString("orderJson")
            val order = remember(orderJson) {
                orderJson?.let {
                    val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Gson().fromJson(decoded, OrderHistoryDto::class.java)
                }
            }
            
            OrderDetailScreen(
                order = order,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Support.route) {
            SupportScreen(
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
