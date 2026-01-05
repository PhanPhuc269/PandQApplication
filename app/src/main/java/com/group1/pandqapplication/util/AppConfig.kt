package com.group1.pandqapplication.util

import com.group1.pandqapplication.BuildConfig

/**
 * Centralized configuration for app-wide constants.
 * Values are read from local.properties via BuildConfig.
 * 
 * To configure, add to local.properties:
 *   DEEP_LINK_DOMAIN=pandq.com
 *   APP_NAME=TechShop
 */
object AppConfig {
    
    /**
     * Deep link domain for sharing products and opening app from browsers.
     * Configured in local.properties: DEEP_LINK_DOMAIN=pandq.com
     */
    val DEEP_LINK_DOMAIN: String = BuildConfig.DEEP_LINK_DOMAIN
    
    /**
     * Full base URL for deep links (HTTPS scheme)
     */
    val DEEP_LINK_BASE_URL: String = "https://$DEEP_LINK_DOMAIN"
    
    /**
     * App custom scheme for internal deep links (FCM notifications, etc.)
     */
    const val APP_SCHEME = "pandqapp"
    
    /**
     * App display name for sharing text
     * Configured in local.properties: APP_NAME=TechShop
     */
    val APP_NAME: String = BuildConfig.APP_NAME
    
    /**
     * Generate a shareable product deep link
     */
    fun getProductDeepLink(productId: String): String {
        return "$DEEP_LINK_BASE_URL/products/$productId"
    }
    
    /**
     * Generate a shareable order deep link
     */
    fun getOrderDeepLink(orderId: String): String {
        return "$DEEP_LINK_BASE_URL/orders/$orderId"
    }
}
