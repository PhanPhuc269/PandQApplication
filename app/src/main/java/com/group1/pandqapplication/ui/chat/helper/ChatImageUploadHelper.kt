package com.group1.pandqapplication.ui.chat.helper

import android.content.Context
import android.util.Log
import com.group1.pandqapplication.shared.data.remote.service.CloudinaryService
import com.group1.pandqapplication.shared.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.net.Uri

/**
 * Helper class for handling image uploads in chat.
 * Simplifies the process of uploading images to Cloudinary
 * and sending them as messages.
 */
class ChatImageUploadHelper(
    private val context: Context,
    private val cloudinaryService: CloudinaryService
) {

    private val tag = "ChatImageUploadHelper"

    /**
     * Upload image from URI and get the Cloudinary URL.
     *
     * @param imageUri The URI of the image to upload
     * @return The Cloudinary public URL, or null if upload fails
     */
    suspend fun uploadImageFromUri(imageUri: String): String? = withContext(Dispatchers.Default) {
        try {
            // Convert URI to file path
            val filePath = FileUtil.getFilePathFromUri(context, Uri.parse(imageUri))
            Log.d(tag, "Image file path: $filePath")

            // Upload to Cloudinary
            val imageUrl = cloudinaryService.uploadImageFromPath(filePath)
            
            if (imageUrl != null) {
                Log.d(tag, "Image uploaded successfully: $imageUrl")
            } else {
                Log.e(tag, "Failed to get image URL from Cloudinary")
            }

            imageUrl
        } catch (e: Exception) {
            Log.e(tag, "Error uploading image from URI: ${e.message}", e)
            null
        }
    }

    /**
     * Get the file name from URI for display purposes.
     *
     * @param imageUri The URI of the image
     * @return The file name, or "Image" if unable to determine
     */
    fun getFileNameFromUri(imageUri: String): String {
        return try {
            val filePath = FileUtil.getFilePathFromUri(context, Uri.parse(imageUri))
            java.io.File(filePath).name
        } catch (e: Exception) {
            Log.w(tag, "Could not get file name from URI", e)
            "Image"
        }
    }
}
