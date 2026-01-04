package com.group1.pandqapplication.util

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val TAG = "FirebaseStorageHelper"

object FirebaseStorageHelper {
    
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    
    /**
     * Upload image to Firebase Storage and return the download URL
     */
    suspend fun uploadImage(uri: Uri): Result<String> {
        return try {
            val fileName = "reviews/${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(fileName)
            
            Log.d(TAG, "Uploading to Firebase Storage: $fileName")
            
            // Upload file
            imageRef.putFile(uri).await()
            
            // Get download URL
            val downloadUrl = imageRef.downloadUrl.await().toString()
            
            Log.d(TAG, "Upload successful! URL: $downloadUrl")
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Log.e(TAG, "Upload failed", e)
            Result.failure(e)
        }
    }
}
