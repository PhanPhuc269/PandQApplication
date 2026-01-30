package com.group1.pandqapplication.ui.personalinfo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.widget.Toast
import com.group1.pandqapplication.util.CloudinaryHelper
import java.io.File
import java.io.FileOutputStream
import com.group1.pandqapplication.R
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onNavigateBack: () -> Unit,
    viewModel: PersonalInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Image picker launcher with Cloudinary upload + auto-save to backend
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        if (uri != null) {
            viewModel.setUploadingImage(true)
            scope.launch {
                try {
                    // Convert Uri to File
                    val file = uriToFile(context, uri)
                    if (file != null && file.exists() && file.length() > 0) {
                        val result = CloudinaryHelper.uploadImage(file)
                        result.onSuccess { url ->
                            // Auto-save to backend after successful upload
                            viewModel.updateAvatarAndSave(url)
                            Toast.makeText(context, "Cập nhật ảnh thành công!", Toast.LENGTH_SHORT).show()
                        }
                        result.onFailure { e ->
                            viewModel.setUploadingImage(false)
                            Toast.makeText(context, "Lỗi upload: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        viewModel.setUploadingImage(false)
                        Toast.makeText(context, "Lỗi đọc file ảnh", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    viewModel.setUploadingImage(false)
                    Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    // Success animation state
    var showSuccessAnimation by remember { mutableStateOf(false) }

    // Show success/error messages
    LaunchedEffect(uiState.successMessage, uiState.error) {
        uiState.successMessage?.let {
            showSuccessAnimation = true
            snackbarHostState.showSnackbar(it)
            delay(300)
            showSuccessAnimation = false
            viewModel.clearMessages()
        }
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 2.dp,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        stringResource(R.string.personal_info_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF111827)
                    )
                }
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Avatar Section with Gradient Border
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Gradient border
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .border(
                                        width = 3.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(primaryColor, primaryColor.copy(alpha = 0.5f))
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            
                            // Avatar
                            AsyncImage(
                                model = uiState.selectedImageUri ?: uiState.avatarUrl.ifEmpty { 
                                    "https://lh3.googleusercontent.com/aida-public/AB6AXuCwILGFmyQOJSWfUNVKWm8accZ99ZMUMajjM5_16dG6r-LbAS5VUTS-SjVchAwiv6T8CUIJPoO9_QIWhQbbjUv2YwJGkrHinCulA75-HDAzfS3IBlDyl5IWJfgMBTJLdOWCV4MuPguh-U1AvZ6LUb_qHUUy_V6357n6jLUAKXeojajAMcxJvqXZmY2bAsliPryIr-4ny0TgV6__D9tF8IEvF4sKVH2DSX1u2kUrTQSdURvOV2aJn__I-w84iZWceHB4qacbhicBJQg" 
                                },
                                contentDescription = "Avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                            )
                            
                            // Camera icon overlay
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(x = (-4).dp, y = (-4).dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor)
                                    .clickable { 
                                        imagePickerLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.isUploadingImage) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Filled.CameraAlt,
                                        contentDescription = "Change photo",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Form Section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        // Full Name Field
                        FormField(
                            label = stringResource(R.string.full_name),
                            value = uiState.fullName,
                            onValueChange = { viewModel.updateFullName(it) },
                            icon = Icons.Filled.Person,
                            primaryColor = primaryColor
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Email Field (Read-only)
                        FormField(
                            label = stringResource(R.string.email),
                            value = uiState.user?.email ?: "",
                            onValueChange = {},
                            icon = Icons.Filled.Email,
                            primaryColor = primaryColor,
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Phone Field
                        FormField(
                            label = stringResource(R.string.phone_number),
                            value = uiState.phone,
                            onValueChange = { viewModel.updatePhone(it) },
                            icon = Icons.Filled.Phone,
                            primaryColor = primaryColor
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Save Button with Animation
                        Button(
                            onClick = { viewModel.saveChanges() },
                            enabled = !uiState.isSaving,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                disabledContainerColor = primaryColor.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            AnimatedContent(
                                targetState = uiState.isSaving,
                                transitionSpec = {
                                    fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                                },
                                label = "button_content"
                            ) { isSaving ->
                                if (isSaving) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.Save,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            stringResource(R.string.save_changes),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
        
        // Success checkmark animation overlay
        AnimatedVisibility(
            visible = showSuccessAnimation,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    primaryColor: Color,
    enabled: Boolean = true
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (enabled) primaryColor else Color(0xFF9CA3AF),
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = Color(0xFFD1D5DB),
                disabledBorderColor = Color(0xFFE5E7EB),
                disabledTextColor = Color(0xFF6B7280),
                focusedLeadingIconColor = primaryColor,
                unfocusedLeadingIconColor = Color(0xFF9CA3AF)
            ),
            singleLine = true
        )
    }
}

private fun uriToFile(context: android.content.Context, uri: android.net.Uri): java.io.File? {
    val contentResolver = context.contentResolver
    val tempFile = java.io.File.createTempFile("avatar_", ".jpg", context.cacheDir)
    tempFile.deleteOnExit()

    return try {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream = java.io.FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
