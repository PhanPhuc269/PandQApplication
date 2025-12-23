package com.group1.pandqapplication.util

import android.util.Log
import com.group1.pandqapplication.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

private const val TAG = "CloudinaryHelper"

/**
 * Cloudinary unsigned upload helper.
 * Credentials are loaded from local.properties via BuildConfig.
 */
object CloudinaryHelper {
    
    // Cloudinary credentials from BuildConfig (loaded from local.properties)
    private val CLOUD_NAME = BuildConfig.CLOUDINARY_CLOUD_NAME
    private val UPLOAD_PRESET = BuildConfig.CLOUDINARY_UPLOAD_PRESET
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
    
    /**
     * Upload image file to Cloudinary and return the secure URL
     */
    suspend fun uploadImage(file: File): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload"
            
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload_preset", UPLOAD_PRESET)
                .addFormDataPart("file", file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
                .build()
            
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            
            Log.d(TAG, "Uploading to Cloudinary...")
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (response.isSuccessful && responseBody != null) {
                val json = JSONObject(responseBody)
                val secureUrl = json.getString("secure_url")
                Log.d(TAG, "Upload successful! URL: $secureUrl")
                Result.success(secureUrl)
            } else {
                Log.e(TAG, "Upload failed: ${response.code} - $responseBody")
                Result.failure(Exception("Upload failed: ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Upload exception", e)
            Result.failure(e)
        }
    }
}
