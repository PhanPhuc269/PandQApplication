package com.group1.pandqapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK

@HiltAndroidApp
class PandQApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize ZaloPay SDK with Sandbox environment
        // App ID 2554 is your sandbox app_id from ZaloPay
        ZaloPaySDK.init(2554, Environment.SANDBOX)
    }
}
