package com.group1.pandqapplication.admin.ui.category

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

@Composable
fun rememberImagePickerLauncher(onImageSelected: (Uri?) -> Unit) = 
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onImageSelected(uri)
    }

fun getImageFileFromUri(context: Context, uri: Uri?): File? {
    if (uri == null) return null
    
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        null
    }
}

suspend fun uploadImageToCloudinary(
    context: Context,
    uri: Uri?,
    cloudName: String,
    uploadPreset: String
): String? {
    if (uri == null) return null
    
    return withContext(Dispatchers.IO) {
        try {
            val imageFile = getImageFileFromUri(context, uri) ?: return@withContext null
            
            Log.d("CloudinaryUpload", "CloudName: $cloudName, Preset: $uploadPreset")
            Log.d("CloudinaryUpload", "Uploading file: ${imageFile.name} (${imageFile.length()} bytes)")
            
            val client = OkHttpClient()
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.name, 
                    okhttp3.RequestBody.create("image/jpeg".toMediaType(), imageFile))
                .addFormDataPart("upload_preset", uploadPreset)
                .addFormDataPart("folder", "categories")
                .build()
            
            val url = "https://api.cloudinary.com/v1_1/$cloudName/image/upload"
            Log.d("CloudinaryUpload", "Uploading to: $url")
            
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            
            val response = client.newCall(request).execute()
            
            Log.d("CloudinaryUpload", "Response code: ${response.code}")
            
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: return@withContext null
                Log.d("CloudinaryUpload", "Response: $responseBody")
                
                val secureUrl = extractSecureUrlFromJson(responseBody)
                Log.d("CloudinaryUpload", "Upload success: $secureUrl")
                secureUrl
            } else {
                val errorBody = response.body?.string() ?: "Unknown error"
                Log.e("CloudinaryUpload", "Upload failed: ${response.code} - $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e("CloudinaryUpload", "Upload exception: ${e.message}", e)
            null
        }
    }
}

private fun extractSecureUrlFromJson(json: String): String? {
    return try {
        // Simple JSON parsing for secure_url
        val startIndex = json.indexOf("\"secure_url\":\"") + 14
        val endIndex = json.indexOf("\"", startIndex)
        if (startIndex > 13 && endIndex > startIndex) {
            json.substring(startIndex, endIndex)
        } else null
    } catch (e: Exception) {
        null
    }
}

suspend fun uploadImageToFirebase(uri: Uri?): String? {
    if (uri == null) return null
    
    return try {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "categories/${System.currentTimeMillis()}.jpg"
        val fileRef = storageRef.child(fileName)
        
        Log.d("FirebaseUpload", "Uploading to: $fileName")
        fileRef.putFile(uri).await()
        val downloadUrl = fileRef.downloadUrl.await().toString()
        Log.d("FirebaseUpload", "Upload success: $downloadUrl")
        downloadUrl
    } catch (e: Exception) {
        Log.e("FirebaseUpload", "Upload failed: ${e.message}", e)
        null
    }
}
