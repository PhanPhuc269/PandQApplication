package com.group1.pandqapplication.shared.data.remote.service

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

/**
 * Service for uploading images to Cloudinary.
 * Handles client-side unsigned uploads using Cloudinary's upload widget.
 */
class CloudinaryService(
    private val cloudName: String,
    private val uploadPreset: String
) {

    private val client = OkHttpClient()
    private val tag = "CloudinaryService"

    /**
     * Upload an image file to Cloudinary using unsigned upload.
     *
     * @param file The image file to upload
     * @return The public URL of the uploaded image, or null if upload fails
     */
    suspend fun uploadImage(file: File): String? = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) {
                Log.e(tag, "File does not exist: ${file.absolutePath}")
                return@withContext null
            }

            val mediaType = "image/jpeg".toMediaType()
            val requestBody = file.asRequestBody(mediaType)
            
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, requestBody)
                .addFormDataPart("upload_preset", uploadPreset)
                .addFormDataPart("cloud_name", cloudName)
                .build()

            val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"

            val request = Request.Builder()
                .url(url)
                .post(multipartBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e(tag, "Upload failed: ${response.code} ${response.message}")
                    return@withContext null
                }

                val responseBody = response.body?.string() ?: return@withContext null
                val jsonResponse = JSONObject(responseBody)
                
                // Return the secure_url from Cloudinary response
                val secureUrl = jsonResponse.optString("secure_url")
                if (secureUrl.isNotEmpty()) {
                    Log.d(tag, "Image uploaded successfully: $secureUrl")
                    return@withContext secureUrl
                }

                Log.e(tag, "No secure_url in response")
                null
            }

        } catch (e: Exception) {
            Log.e(tag, "Error uploading image to Cloudinary", e)
            null
        }
    }

    /**
     * Upload an image file from a URI string path.
     *
     * @param filePath The absolute path to the image file
     * @return The public URL of the uploaded image, or null if upload fails
     */
    suspend fun uploadImageFromPath(filePath: String): String? {
        return uploadImage(File(filePath))
    }
}
