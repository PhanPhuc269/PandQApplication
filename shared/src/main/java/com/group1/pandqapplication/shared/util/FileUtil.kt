package com.group1.pandqapplication.shared.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

/**
 * Utility functions for file operations.
 */
object FileUtil {

    /**
     * Convert URI to actual file path.
     * For content:// URIs, we need to copy to app cache.
     */
    fun getFilePathFromUri(context: Context, uri: Uri): String {
        return when {
            uri.scheme == ContentResolver.SCHEME_CONTENT -> {
                // Copy content URI to cache file
                val cacheDir = context.cacheDir
                val fileName = getFileNameFromUri(context, uri) ?: "image_${System.currentTimeMillis()}.jpg"
                val cacheFile = File(cacheDir, fileName)
                
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
                
                cacheFile.absolutePath
            }
            uri.scheme == "file" -> {
                uri.path ?: ""
            }
            else -> uri.toString()
        }
    }

    /**
     * Get file name from URI.
     */
    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        return when {
            uri.scheme == ContentResolver.SCHEME_CONTENT -> {
                val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val index = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                        if (index >= 0) it.getString(index) else null
                    } else {
                        null
                    }
                }
            }
            uri.scheme == "file" -> {
                uri.lastPathSegment
            }
            else -> null
        }
    }
}
