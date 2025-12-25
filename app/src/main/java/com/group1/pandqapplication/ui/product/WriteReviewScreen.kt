package com.group1.pandqapplication.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.data.remote.dto.ProductDetailDto
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import android.util.Log
import androidx.compose.runtime.rememberCoroutineScope
import com.group1.pandqapplication.util.CloudinaryHelper
import kotlinx.coroutines.launch

private const val TAG = "WriteReviewScreen"

@Composable
fun WriteReviewScreen(
    product: ProductDetailDto?,
    onBackClick: () -> Unit,
    onSubmitClick: (Int, String, List<String>) -> Unit,
    onUploadImage: (java.io.File, (Result<String>) -> Unit) -> Unit = { _, _ -> },
    primaryColor: Color = Color(0xFF007AFF),
    isSubmitting: Boolean = false,
    errorMessage: String? = null,
    onDismissError: () -> Unit = {}
) {
    var rating by remember { mutableIntStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var imageUrls by remember { mutableStateOf(listOf<String>()) }
    var isUploading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        Log.d(TAG, "Image picker result: uri = $uri")
        if (uri != null) {
            isUploading = true
            scope.launch {
                try {
                    val file = uriToFile(context, uri)
                    if (file != null && file.exists() && file.length() > 0) {
                        Log.d(TAG, "Uploading to Cloudinary... file size: ${file.length()} bytes")
                        val result = CloudinaryHelper.uploadImage(file)
                        isUploading = false
                        result.onSuccess { url ->
                            Log.d(TAG, "Cloudinary upload success! URL: $url")
                            imageUrls = imageUrls + url
                            Toast.makeText(context, "Upload thành công!", Toast.LENGTH_SHORT).show()
                        }
                        result.onFailure { e ->
                            Log.e(TAG, "Cloudinary upload failed", e)
                            Toast.makeText(context, "Lỗi upload: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        isUploading = false
                        Log.e(TAG, "File conversion failed")
                        Toast.makeText(context, "Lỗi đọc file ảnh", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    isUploading = false
                    Log.e(TAG, "Exception during Cloudinary upload", e)
                    Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.d(TAG, "User cancelled image selection")
        }
    }

    // Error Alert Dialog
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = onDismissError,
            confirmButton = {
                TextButton(onClick = onDismissError) {
                    Text("Đã hiểu", color = primaryColor)
                }
            },
            icon = {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Không thể đánh giá", fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(top = 60.dp)
    ) {
        // 1. Top App Bar
        Surface(shadowElevation = 1.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = primaryColor
                    )
                }
                Text(
                    text = "Đánh giá sản phẩm",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = primaryColor,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Gửi",
                        fontSize = 16.sp,
                        color = primaryColor,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            if (!isSubmitting) onSubmitClick(rating, comment, imageUrls)
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(1f)
        ) {
            // 2. Product Info Card
            if (product != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = product.thumbnailUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF4F4F5))
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF333333),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Màu: Đen, RAM: 16GB",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Rating Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chất lượng sản phẩm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..5).forEach { star ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star $star",
                            tint = if (star <= rating) Color(0xFFFF9500) else Color(0xFFE0E0E0),
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { rating = star }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when (rating) {
                        5 -> "Tuyệt vời"
                        4 -> "Rất tốt"
                        3 -> "Bình thường"
                        2 -> "Tệ"
                        else -> "Rất tệ"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Comment Box
            Text(
                text = "Chia sẻ thêm",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF333333)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Hãy chia sẻ cảm nhận của bạn về sản phẩm...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Image Uploader
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hình ảnh thực tế",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Gửi kèm ảnh để nhận thêm ưu đãi!",
                    fontSize = 12.sp,
                    color = Color(0xFF8E8E93)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageUrls) { url ->
                    Box(modifier = Modifier.aspectRatio(1f)) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(6.dp)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .clickable { imageUrls = imageUrls - url },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                if (imageUrls.size < 5) {
                    item {
                        Column(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable { launcher.launch("image/*") },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isUploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = primaryColor
                                )
                            } else {
                                Icon(
                                    Icons.Filled.AddPhotoAlternate,
                                    contentDescription = null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(32.dp)
                                )
                                Text(
                                    "Thêm ảnh",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryColor,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Button
        Surface(
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { if (!isSubmitting) onSubmitClick(rating, comment, imageUrls) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    enabled = !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Gửi đánh giá", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun uriToFile(context: android.content.Context, uri: android.net.Uri): java.io.File? {
    val contentResolver = context.contentResolver
    val tempFile = java.io.File.createTempFile("upload_", ".jpg", context.cacheDir)
    tempFile.deleteOnExit()

    try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream = java.io.FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
