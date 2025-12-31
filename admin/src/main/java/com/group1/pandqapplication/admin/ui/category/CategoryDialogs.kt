package com.group1.pandqapplication.admin.ui.category

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.group1.pandqapplication.admin.utils.CloudinaryConfig
import com.group1.pandqapplication.shared.data.remote.dto.CategoryDto
import com.group1.pandqapplication.shared.ui.theme.CategoryBackground
import com.group1.pandqapplication.shared.ui.theme.CategorySurface
import com.group1.pandqapplication.shared.ui.theme.CategoryTextPrimary
import com.group1.pandqapplication.shared.ui.theme.CategoryTextSecondary
import java.util.UUID
import kotlinx.coroutines.launch

@Composable
fun AddEditCategoryDialog(
    isOpen: Boolean,
    category: CategoryDto? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, description: String?, imageUrl: String?, parentId: UUID?) -> Unit,
    categories: List<CategoryDto> = emptyList()
) {
    if (!isOpen) return

    var name by remember { mutableStateOf(category?.name ?: "") }
    var description by remember { mutableStateOf(category?.description ?: "") }
    var imageUrl by remember { mutableStateOf(category?.imageUrl ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedParentId by remember { mutableStateOf<UUID?>(category?.parentId) }
    var expandParentDropdown by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        if (uri != null) {
            selectedImageUri = uri
            isUploading = true
            uploadError = null
            scope.launch {
                try {
                    // Upload lên Cloudinary
                    val cloudName = CloudinaryConfig.CLOUD_NAME
                    val uploadPreset = CloudinaryConfig.UPLOAD_PRESET
                    
                    android.util.Log.d("CategoryDialog", "CloudName: '$cloudName'")
                    android.util.Log.d("CategoryDialog", "UploadPreset: '$uploadPreset'")
                    
                    val uploadedUrl = uploadImageToCloudinary(context, uri, cloudName, uploadPreset)
                    
                    if (uploadedUrl != null) {
                        imageUrl = uploadedUrl
                        uploadError = null
                        android.util.Log.d("ImagePicker", "Image uploaded: $uploadedUrl")
                    } else {
                        uploadError = "Lỗi upload ảnh. Kiểm tra logcat để chi tiết."
                        android.util.Log.e("ImagePicker", "Upload returned null")
                    }
                } catch (e: Exception) {
                    uploadError = "Lỗi: ${e.message}"
                    android.util.Log.e("ImagePicker", "Error: ${e.message}", e)
                } finally {
                    isUploading = false
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(12.dp))
                .background(CategorySurface)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (category == null) "Thêm danh mục mới" else "Chỉnh sửa danh mục",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CategoryTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CategoryTextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name Field
            Column {
                Text(
                    text = "Tên danh mục *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CategoryTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CategoryBackground),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = CategoryTextPrimary,
                            fontSize = 14.sp
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                        decorationBox = { innerTextField ->
                            if (name.isEmpty()) {
                                Text(
                                    text = "Nhập tên danh mục",
                                    color = CategoryTextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Field
            Column {
                Text(
                    text = "Mô tả",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CategoryTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CategoryBackground),
                    verticalAlignment = Alignment.Top
                ) {
                    BasicTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            color = CategoryTextPrimary,
                            fontSize = 14.sp
                        ),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                        decorationBox = { innerTextField ->
                            if (description.isEmpty()) {
                                Text(
                                    text = "Nhập mô tả danh mục",
                                    color = CategoryTextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Image URL Field with Preview
            Column {
                Text(
                    text = "Hình ảnh danh mục",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CategoryTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Error message
                if (uploadError != null) {
                    Text(
                        text = uploadError!!,
                        fontSize = 12.sp,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }
                
                // Image Preview
                if (imageUrl.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CategoryBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Preview hình ảnh danh mục",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        if (isUploading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Image Picker Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CategoryBackground),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUploading) "Đang tải lên..." 
                               else if (imageUrl.isNotEmpty()) "Ảnh đã chọn" 
                               else "Chưa chọn ảnh",
                        color = if (imageUrl.isNotEmpty()) CategoryTextPrimary else CategoryTextSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                    )
                    
                    if (isUploading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 12.dp)
                        )
                    } else {
                        IconButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Chọn hình ảnh",
                                tint = CategoryTextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Parent Category Dropdown
            if (categories.isNotEmpty()) {
                Column {
                    Text(
                        text = "Danh mục cha",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = CategoryTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(CategoryBackground)
                                .padding(horizontal = 12.dp)
                                .clickable { expandParentDropdown = !expandParentDropdown },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedParentId?.let { parentId ->
                                    categories.find { it.id == parentId }?.name ?: "Chọn danh mục cha"
                                } ?: "Không có",
                                color = CategoryTextPrimary,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        DropdownMenu(
                            expanded = expandParentDropdown,
                            onDismissRequest = { expandParentDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Không có") },
                                onClick = {
                                    selectedParentId = null
                                    expandParentDropdown = false
                                }
                            )
                            
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedParentId = category.id
                                        expandParentDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CategoryBackground
                    )
                ) {
                    Text(
                        text = "Hủy",
                        color = CategoryTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onSave(name, description, imageUrl, selectedParentId)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = name.isNotBlank()
                ) {
                    Text(
                        text = "Lưu",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(
    isOpen: Boolean,
    categoryName: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(12.dp))
                .background(CategorySurface)
                .padding(24.dp)
        ) {
            Text(
                text = "Xác nhận xóa",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CategoryTextPrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bạn có chắc chắn muốn xóa danh mục \"$categoryName\" không?",
                fontSize = 14.sp,
                color = CategoryTextSecondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CategoryBackground
                    )
                ) {
                    Text("Hủy", color = CategoryTextPrimary)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Xóa", color = Color.White)
                }
            }
        }
    }
}
