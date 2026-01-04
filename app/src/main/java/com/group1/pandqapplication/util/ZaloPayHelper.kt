package com.group1.pandqapplication.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Helper class for ZaloPay SDK integration
 * 
 * IMPORTANT: To fully integrate ZaloPay, you need to:
 * 1. Download zpdk-release.aar from ZaloPay Developer Portal (https://mc.zalopay.vn)
 * 2. Place it in app/libs/ folder
 * 3. Add to build.gradle: implementation(files("libs/zpdk-release-vx.x.aar"))
 * 4. Initialize SDK in Application class: ZaloPaySDK.init(appId, Environment)
 * 
 * For now, this helper provides a mock implementation that opens ZaloPay app if installed.
 */
object ZaloPayHelper {
    
    private const val ZALOPAY_PACKAGE = "vn.com.vng.zalopay"
    private const val ZALOPAY_SANDBOX_PACKAGE = "vn.com.vng.zalopay.zlpscanner"
    
    // ZaloPay App ID (Get from ZaloPay Merchant Portal)
    // TODO: Replace with your actual app_id
    private const val APP_ID = "2553" // Demo/Sandbox app_id from ZaloPay
    
    /**
     * Check if ZaloPay app is installed
     */
    fun isZaloPayInstalled(context: Context): Boolean {
        return try {
            val pm = context.packageManager
            pm.getPackageInfo(ZALOPAY_PACKAGE, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Open ZaloPay app or Play Store if not installed
     */
    fun openZaloPayApp(context: Context) {
        if (isZaloPayInstalled(context)) {
            val intent = context.packageManager.getLaunchIntentForPackage(ZALOPAY_PACKAGE)
            intent?.let { context.startActivity(it) }
        } else {
            openZaloPayOnPlayStore(context)
        }
    }
    
    /**
     * Open ZaloPay on Play Store
     */
    fun openZaloPayOnPlayStore(context: Context) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$ZALOPAY_PACKAGE")
                )
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$ZALOPAY_PACKAGE")
                )
            )
        }
    }
    
    /**
     * Create a payment order
     * 
     * In production, this should call your backend to create an order
     * and return a zp_trans_token from ZaloPay's CreateOrder API
     * 
     * @param amount Payment amount in VND
     * @param description Order description
     * @return zp_trans_token if successful, null otherwise
     */
    suspend fun createOrder(amount: Long, description: String): String? {
        // TODO: Call your backend API to create order
        // Backend should call ZaloPay's CreateOrder API and return zp_trans_token
        // 
        // Example backend endpoint: POST /api/v1/payments/zalopay/create-order
        // Request body: { "amount": 2480000, "description": "Order #123" }
        // Response: { "zp_trans_token": "xxxxxx", "order_id": "order_123" }
        
        return null // Replace with actual API call
    }
    
    /**
     * Pay with ZaloPay using the transaction token
     * 
     * When ZaloPay SDK is integrated, call:
     * ZaloPaySDK.getInstance().payOrder(activity, zpTransToken, object: PayOrderListener {
     *     override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransId: String) {
     *         // Payment successful
     *     }
     *     override fun onPaymentCanceled(zpTransToken: String, appTransId: String) {
     *         // Payment cancelled
     *     }
     *     override fun onPaymentError(errorCode: ZaloPayError, zpTransToken: String, appTransId: String) {
     *         // Payment error
     *         if (errorCode == ZaloPayError.ZALO_PAY_NOT_INSTALLED) {
     *             openZaloPayOnPlayStore(context)
     *         }
     *     }
     * })
     */
    fun payOrder(zpTransToken: String) {
        // TODO: Implement with ZaloPay SDK
        // ZaloPaySDK.getInstance().payOrder(activity, zpTransToken, listener)
    }
}
