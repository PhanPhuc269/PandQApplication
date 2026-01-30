package com.group1.pandqapplication.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object Onboarding : Screen("onboarding")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object Cart : Screen("cart_screen")
    data object Search : Screen("search_screen?categoryId={categoryId}") {
        fun createRoute(categoryId: String? = null): String {
            return if (categoryId != null) {
                "search_screen?categoryId=$categoryId"
            } else {
                "search_screen"
            }
        }
    }
    data object Splash : Screen("splash")
    data object Checkout : Screen("checkout/{orderId}") {
        fun createRoute(orderId: String) = "checkout/$orderId"
    }
    data object OrderTracking : Screen("order_tracking?orderId={orderId}") {
        fun createRoute(orderId: String? = null): String {
            return if (orderId != null) {
                "order_tracking?orderId=$orderId"
            } else {
                "order_tracking"
            }
        }
    }
    data object Support : Screen("support")
    data object ShippingAddress : Screen("shipping_address")
    data object PersonalInfo : Screen("personal_info")
    data object AddressList : Screen("address_list")
    data object AddEditAddress : Screen("add_edit_address?addressId={addressId}") {
        fun createRoute(addressId: String? = null): String {
            return if (addressId != null) {
                "add_edit_address?addressId=$addressId"
            } else {
                "add_edit_address"
            }
        }
    }
    data object MapPicker : Screen("map_picker")
    data object OrderSuccess : Screen("order_success/{orderId}") {
        fun createRoute(orderId: String) = "order_success/$orderId"
    }
    data object WriteReview : Screen("write_review/{productId}") {
        fun createRoute(productId: String) = "write_review/$productId"
    }
    data object UserGuide : Screen("user_guide")
    data object ChatScreen : Screen("chat_screen/{productId}") {
        fun createRoute(productId: String, productName: String = "", productImage: String = "", productPrice: String = ""): String {
            // Store product info temporarily in a way that doesn't break routing
            // For now, just use productId - product info will be fetched from API
            return "chat_screen/$productId"
        }
        
        fun createRouteWithProduct(productId: String, productName: String, productImage: String, productPrice: String): String {
            return "chat_screen/$productId?name=${java.net.URLEncoder.encode(productName, "UTF-8")}&image=${java.net.URLEncoder.encode(productImage, "UTF-8")}&price=${java.net.URLEncoder.encode(productPrice, "UTF-8")}"
        }
    }
    data object ChatList : Screen("chat_list")
    data object VoucherCenter : Screen("voucher_center")

    data object Policy : Screen("policy")
    data object ProductManagement : Screen("product_management")
    data object AddProduct : Screen("add_product?productId={productId}") {
        fun createRoute(productId: String? = null): String {
            return if (productId != null) {
                "add_product?productId=$productId"
            } else {
                "add_product"
            }
        }
    }

}
