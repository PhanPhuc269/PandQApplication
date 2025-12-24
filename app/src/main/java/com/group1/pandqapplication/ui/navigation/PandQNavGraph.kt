package com.group1.pandqapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderTracking.createRoute(orderId))
                },
                onPersonalInfoClick = {
                    navController.navigate(Screen.PersonalInfo.route)
                },
                onAddressListClick = {
                    navController.navigate(Screen.AddressList.route)
                },                    
                onNavigateToOrder = { orderId ->
                    navController.navigate(Screen.OrderTracking.createRoute(orderId))
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
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
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
                },
                userId = mainViewModel.getCurrentUserId()
            )
        }
        composable(Screen.Cart.route) {
            com.group1.pandqapplication.ui.cart.CartScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCheckoutClick = { orderId ->
                    navController.navigate(Screen.Checkout.createRoute(orderId))
                },
                userId = mainViewModel.getCurrentUserId()
            )
        }
        composable(
            route = Screen.Checkout.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            CheckoutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditAddressClick = {
                    navController.navigate(Screen.AddressList.route)
                },
                onPaymentSuccess = { paidOrderId ->
                    // Navigate to success screen and clear back stack to checkout
                    navController.navigate(Screen.OrderSuccess.createRoute(paidOrderId)) {
                        popUpTo(Screen.Checkout.route) { inclusive = true }
                    }
                },
                orderId = orderId
            )
        }
        composable(
            route = Screen.OrderTracking.route,
            arguments = listOf(navArgument("orderId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            OrderTrackingScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                orderId = orderId,
                onReviewClick = { productId, _ ->
                    navController.navigate(Screen.WriteReview.createRoute(productId))
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
        composable(Screen.PersonalInfo.route) {
            com.group1.pandqapplication.ui.personalinfo.PersonalInfoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.AddressList.route) {
            com.group1.pandqapplication.ui.address.AddressListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddAddress = {
                    navController.navigate(Screen.AddEditAddress.createRoute())
                },
                onEditAddress = { addressId ->
                    navController.navigate(Screen.AddEditAddress.createRoute(addressId))
                },
                onSelectAddress = { _ ->
                    // After selecting address (and setting as default), pop back to checkout
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.AddEditAddress.route,
            arguments = listOf(
                navArgument("addressId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            com.group1.pandqapplication.ui.address.AddEditAddressScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMap = {
                    navController.navigate(Screen.MapPicker.route)
                }
            )
        }
        composable(Screen.MapPicker.route) {
            com.group1.pandqapplication.ui.address.MapPickerScreen(
                onLocationSelected = { lat, lon, address ->
                    // Save to singleton
                    com.group1.pandqapplication.ui.address.MapSelectionHolder.setAddress(address)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
        )
        }
        composable(
            route = Screen.OrderSuccess.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val successOrderId = backStackEntry.arguments?.getString("orderId") ?: ""
            com.group1.pandqapplication.ui.order.OrderSuccessScreen(
                orderId = successOrderId,
                onCloseClick = {
                    // Go back to home, clearing the payment flow from back stack
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onTrackOrderClick = { orderId ->
                    navController.navigate(Screen.OrderTracking.createRoute(orderId))
                },
                onContinueShoppingClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Screen.WriteReview.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) {
            // Use ProductDetailViewModel for review submission
            // ViewModel will auto-load product from SavedStateHandle "productId"
            val viewModel: com.group1.pandqapplication.ui.product.ProductDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            // Handle success - navigate back to Home (which has Orders tab)
            androidx.compose.runtime.LaunchedEffect(uiState.reviewSubmitSuccess) {
                if (uiState.reviewSubmitSuccess) {
                    // Navigate back to Home and clear the back stack
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            }
            
            com.group1.pandqapplication.ui.product.WriteReviewScreen(
                product = uiState.product,
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { rating, comment, imageUrls ->
                    viewModel.submitReview(rating, comment, imageUrls)
                },
                primaryColor = com.group1.pandqapplication.shared.ui.theme.Primary,
                isSubmitting = uiState.isSubmittingReview,
                errorMessage = uiState.reviewSubmitError,
                onDismissError = { viewModel.clearReviewSubmitState() }
            )
        }
    }
}
