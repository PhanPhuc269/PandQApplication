package com.group1.pandqapplication.shared.data.remote

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp Interceptor that adds Firebase ID token to the Authorization header
 * for authenticated API requests.
 */
class AuthInterceptor : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // Get current Firebase user
        val currentUser = FirebaseAuth.getInstance().currentUser
        
        if (currentUser == null) {
            // No user logged in, proceed without auth header
            return chain.proceed(originalRequest)
        }
        
        // Get Firebase ID token
        val token = runBlocking {
            try {
                currentUser.getIdToken(false).await().token
            } catch (e: Exception) {
                null
            }
        }
        
        return if (token != null) {
            // Add Authorization header with Bearer token
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
