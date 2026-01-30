package com.group1.pandqapplication.ui.chat.screen

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.group1.pandqapplication.shared.data.remote.dto.ChatMessage
import com.group1.pandqapplication.shared.data.remote.dto.MessageType
import com.group1.pandqapplication.ui.chat.viewmodel.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChatScreen(
    productId: String,
    onBackClick: () -> Unit,
    userId: String,
    onProductClick: (String) -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }
    var isUploadingImage by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // State for fullscreen image viewer
    var fullScreenImageUrl by remember { mutableStateOf<String?>(null) }
    
    // State for media picker dialog
    var showMediaPickerDialog by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { mediaUri: Uri? ->
        if (mediaUri != null) {
            isUploadingImage = true
            scope.launch {
                viewModel.sendImage(state.chat?.id ?: return@launch, mediaUri.toString())
                isUploadingImage = false
            }
        }
    }
    
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { videoUri: Uri? ->
        if (videoUri != null) {
            isUploadingImage = true
            scope.launch {
                viewModel.sendImage(state.chat?.id ?: return@launch, videoUri.toString())
                isUploadingImage = false
            }
        }
    }
    
    // Media picker dialog
    if (showMediaPickerDialog) {
        AlertDialog(
            onDismissRequest = { showMediaPickerDialog = false },
            title = { Text("Chọn loại media", color = Color.White) },
            containerColor = Color(0xFF2a2a2a),
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Image option
                    Surface(
                        onClick = {
                            showMediaPickerDialog = false
                            imagePickerLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3a3a3a),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color(0xFFEC6B45),
                                modifier = Modifier.size(24.dp)
                            )
                            Text("Chọn ảnh", color = Color.White, fontSize = 16.sp)
                        }
                    }
                    // Video option
                    Surface(
                        onClick = {
                            showMediaPickerDialog = false
                            videoPickerLauncher.launch("video/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3a3a3a),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(24.dp)
                            )
                            Text("Chọn video", color = Color.White, fontSize = 16.sp)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showMediaPickerDialog = false }) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )
    }
    
    // Fullscreen image dialog
    if (fullScreenImageUrl != null) {
        FullScreenImageDialog(
            imageUrl = fullScreenImageUrl!!,
            onDismiss = { fullScreenImageUrl = null }
        )
    }

    LaunchedEffect(Unit) {
        // First, load product context from holder (set by ProductDetailViewModel.prepareForChat())
        viewModel.loadProductContextFromHolder()
        // Then get/create the general chat
        viewModel.getGeneralChat()
    }

    // Auto-refresh messages every 3 seconds to get new messages from admin
    LaunchedEffect(state.chat?.id) {
        state.chat?.id?.let { chatId ->
            while (true) {
                kotlinx.coroutines.delay(3000)
                viewModel.refreshMessages(chatId)
            }
        }
    }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(state.messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(Color(0xFFF5F5F5))
    ) {
        // HEADER
        ChatHeaderBar(
            productName = state.chat?.productName ?: "Product Chat",
            adminName = "Support Admin",
            onBackClick = onBackClick
        )

        // MESSAGES LIST
        Box(modifier = Modifier
            .weight(1f)
            .background(Color(0xFFFFFFFF))
        ) {
            if (state.isLoading && state.messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFEC6B45))
                }
            } else if (state.messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Bắt đầu cuộc trò chuyện",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Render messages with product dividers
                    val messages = state.messages
                    var lastProductId: String? = null
                    
                    messages.forEachIndexed { index, message ->
                        // Check if this message has a new product context
                        val currentProductContextId = message.productContextId
                        val showDivider = currentProductContextId != null && currentProductContextId != lastProductId
                        
                        if (showDivider && currentProductContextId != null) {
                            item(key = "divider_$index") {
                                ProductDividerCard(
                                    productId = currentProductContextId,
                                    productName = message.productContextName ?: "Sản phẩm",
                                    productImage = message.productContextImage ?: "",
                                    productPrice = message.productContextPrice ?: "",
                                    onClick = { onProductClick(currentProductContextId) }
                                )
                            }
                            lastProductId = currentProductContextId
                        }
                        
                        item(key = "message_${message.id}") {
                            ChatMessageBubble(
                                message = message,
                                isOwn = message.senderRole == "CUSTOMER",
                                adminAvatar = "S",
                                onImageClick = { imageUrl ->
                                    fullScreenImageUrl = imageUrl
                                }
                            )
                        }
                    }
                }
            }
        }

        // QUICK REPLY BUTTONS
        if (state.chat != null && state.messages.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuickReplyButton(
                    text = "Còn hàng không?",
                    onClick = {
                        messageText = "Còn hàng không?"
                        scope.launch {
                            viewModel.sendMessage(state.chat!!.id, "Còn hàng không?")
                            messageText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                QuickReplyButton(
                    text = "Chính sách bảo hành",
                    onClick = {
                        messageText = "Chính sách bảo hành"
                        scope.launch {
                            viewModel.sendMessage(state.chat!!.id, "Chính sách bảo hành")
                            messageText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                QuickReplyButton(
                    text = "Giao hàng",
                    onClick = {
                        messageText = "Giao hàng"
                        scope.launch {
                            viewModel.sendMessage(state.chat!!.id, "Giao hàng")
                            messageText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // INPUT AREA
        ChatInputBar(
            messageText = messageText,
            onMessageChange = { messageText = it },
            onSendClick = {
                if (messageText.isNotBlank() && state.chat != null) {
                    viewModel.sendMessage(state.chat!!.id, messageText)
                    messageText = ""
                }
            },
            onImageClick = {
                showMediaPickerDialog = true
            },
            isLoading = state.isSending || isUploadingImage,
            isUploadingImage = isUploadingImage
        )

        // ERROR MESSAGE
        if (state.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB3261E))
                    .padding(12.dp)
            ) {
                Text(
                    state.error!!,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ChatHeaderBar(
    productName: String,
    adminName: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFFF0F0F0),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1C1C1E)
                )
            }

            // Admin Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEC6B45)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    adminName.take(1),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    adminName,
                    color = Color(0xFF1C1C1E),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    "Đang hoạt động",
                    color = Color(0xFF4CAF50),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            // Phone Call Button - opens dialer with hotline number
            IconButton(onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:190012345")
                }
                context.startActivity(intent)
            }) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Gọi hotline",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ProductInfoCard(
    productName: String,
    productPrice: String,
    productImage: String?,
    subject: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3a3a3a))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!productImage.isNullOrEmpty()) {
                AsyncImage(
                    model = productImage,
                    contentDescription = "Product",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    subject,
                    fontSize = 11.sp,
                    color = Color(0xFFEC6B45),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    productName,
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    productPrice,
                    fontSize = 13.sp,
                    color = Color(0xFFEC6B45),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    isOwn: Boolean,
    adminAvatar: String,
    onImageClick: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start
    ) {
        if (!isOwn) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6B5B5B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    adminAvatar,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (isOwn) Color(0xFFEC6B45) else Color(0xFFEEEEEE),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isOwn) 16.dp else 4.dp,
                        bottomEnd = if (isOwn) 4.dp else 16.dp
                    )
                )
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isOwn) 16.dp else 4.dp,
                        bottomEnd = if (isOwn) 4.dp else 16.dp
                    )
                )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Message text - only show if NOT an IMAGE type message (to hide filename)
                if (message.message.isNotEmpty() && message.messageType != MessageType.IMAGE) {
                    Text(
                        message.message,
                        color = if (isOwn) Color.White else Color(0xFF1C1C1E),
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                }

                // Image/Video - clickable to view fullscreen
                if (!message.imageUrl.isNullOrEmpty()) {
                    if (message.message.isNotEmpty() && message.messageType != MessageType.IMAGE) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    // Check if it's a video based on URL
                    val isVideo = message.imageUrl!!.contains("/video/") || 
                                  message.imageUrl!!.endsWith(".mp4") ||
                                  message.imageUrl!!.endsWith(".mov") ||
                                  message.imageUrl!!.endsWith(".avi") ||
                                  message.imageUrl!!.endsWith(".webm")
                    
                    val context = LocalContext.current
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0E0E0))
                            .clickable { 
                                if (isVideo) {
                                    // Open video with external player
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(Uri.parse(message.imageUrl), "video/*")
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Không tìm thấy ứng dụng để phát video", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    onImageClick(message.imageUrl!!)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isVideo) {
                            // Video thumbnail with play icon
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayCircle,
                                    contentDescription = "Play video",
                                    tint = Color.White,
                                    modifier = Modifier.size(64.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Nhấn để xem video",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            }
                        } else {
                            // Image display
                            var isLoading by remember { mutableStateOf(true) }
                            
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp),
                                    color = Color(0xFFEC6B45),
                                    strokeWidth = 2.dp
                                )
                            }
                            
                            AsyncImage(
                                model = message.imageUrl,
                                contentDescription = "Chat image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                onSuccess = { isLoading = false },
                                onError = { isLoading = false }
                            )
                        }
                    }
                }

                // Time + Read status
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        formatTime(message.createdAt),
                        color = if (isOwn) Color.White.copy(alpha = 0.6f) else Color.Gray,
                        fontSize = 11.sp
                    )

                    if (isOwn) {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            modifier = Modifier.size(14.dp),
                            tint = if (message.isRead) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickReplyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = 1.5.dp, color = Color(0xFFEC6B45)),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFEC6B45),
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun ChatInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onImageClick: () -> Unit,
    isLoading: Boolean,
    isUploadingImage: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        color = Color(0xFFF0F0F0),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Image button
            if (isUploadingImage) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFFEC6B45)
                    )
                }
            } else {
                IconButton(
                    onClick = onImageClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFFE0E0E0),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Upload image",
                        tint = Color(0xFFEC6B45),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Text field
            TextField(
                value = messageText,
                onValueChange = onMessageChange,
                placeholder = {
                    Text(
                        "Viết tin nhắn...",
                        color = Color(0xFF999999),
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp, max = 100.dp)
                    .clip(RoundedCornerShape(24.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFFFFFF),
                    focusedContainerColor = Color(0xFFFFFFFF),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedTextColor = Color(0xFF1C1C1E),
                    focusedTextColor = Color(0xFF1C1C1E)
                ),
                singleLine = false,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = Color(0xFF1C1C1E))
            )

            // Send button
            IconButton(
                onClick = onSendClick,
                enabled = !isLoading && messageText.isNotBlank(),
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (!isLoading && messageText.isNotBlank()) Color(0xFFEC6B45) else Color(0xFFDCDCDC),
                        shape = CircleShape
                    )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (!isLoading && messageText.isNotBlank()) Color.White else Color(0xFF999999),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Product divider card - shows "ĐANG HỎI VỀ" with product info
 * Clickable to navigate to product detail
 */
@Composable
private fun ProductDividerCard(
    productId: String,
    productName: String,
    productImage: String,
    productPrice: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Product label
        Text(
            "ĐANG HỎI VỀ",
            fontSize = 11.sp,
            color = Color(0xFFB8860B),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        
        // Product card - clickable to go to product detail
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    color = Color(0xFF3a3a3a),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onClick() }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            if (productImage.isNotEmpty()) {
                AsyncImage(
                    model = productImage,
                    contentDescription = productName,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF2a2a2a), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            // Product Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    productName,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                if (productPrice.isNotEmpty()) {
                    Text(
                        productPrice,
                        fontSize = 13.sp,
                        color = Color(0xFFEC6B45),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatTime(createdAt: String?): String {
    if (createdAt == null || createdAt.isEmpty()) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(createdAt)
        if (date != null) outputFormat.format(date) else ""
    } catch (e: Exception) {
        ""
    }
}

private fun formatPrice(productId: String): String {
    // Mock price - replace with actual price from product data
    return "34.990.000đ"
}

/**
 * Fullscreen image dialog with zoom and download functionality
 */
@Composable
private fun FullScreenImageDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isSaving by remember { mutableStateOf(false) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {
            // Image with zoom and pan
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Full screen image",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX,
                        translationY = offsetY
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 5f)
                            if (scale > 1f) {
                                offsetX += pan.x
                                offsetY += pan.y
                            } else {
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    },
                contentScale = ContentScale.Fit
            )
            
            // Close button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // Download button
            IconButton(
                onClick = {
                    if (!isSaving) {
                        isSaving = true
                        scope.launch {
                            saveImageToGallery(context, imageUrl) { success ->
                                isSaving = false
                                val message = if (success) "Đã lưu ảnh vào thư viện" else "Không thể lưu ảnh"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            // Instructions
            Text(
                text = "Pinch để zoom • Vuốt để di chuyển",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }
    }
}

/**
 * Save image from URL to device gallery
 */
private suspend fun saveImageToGallery(
    context: Context,
    imageUrl: String,
    onComplete: (Boolean) -> Unit
) {
    withContext(Dispatchers.IO) {
        try {
            // Load image using Coil
            val loader = coil.ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            
            val result = loader.execute(request)
            val bitmap = (result.drawable)?.toBitmap()
            
            if (bitmap != null) {
                val filename = "chat_image_${System.currentTimeMillis()}.jpg"
                var outputStream: OutputStream? = null
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+ use MediaStore
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PandQ")
                    }
                    
                    val uri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    
                    if (uri != null) {
                        outputStream = context.contentResolver.openOutputStream(uri)
                    }
                } else {
                    // Older Android versions
                    @Suppress("DEPRECATION")
                    val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val pandqDir = java.io.File(imagesDir, "PandQ")
                    if (!pandqDir.exists()) pandqDir.mkdirs()
                    val imageFile = java.io.File(pandqDir, filename)
                    outputStream = java.io.FileOutputStream(imageFile)
                }
                
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                
                withContext(Dispatchers.Main) {
                    onComplete(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onComplete(false)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onComplete(false)
            }
        }
    }
}
