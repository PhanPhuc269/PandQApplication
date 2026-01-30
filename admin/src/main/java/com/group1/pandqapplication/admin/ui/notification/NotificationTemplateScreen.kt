package com.group1.pandqapplication.admin.ui.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.NotificationTemplateDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTemplateScreen(
    viewModel: NotificationTemplateViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newBody by remember { mutableStateOf("") }
    var editingTemplateId by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var selectedTargetUrl by remember { mutableStateOf<String?>(null) }
    var showTargetUrlDropdown by remember { mutableStateOf(false) }
    
    // Predefined target destinations
    val targetOptions = listOf(
        "" to "Không chọn (Mặc định)",
        "pandq://home" to "Trang chủ",
        "pandq://promotions" to "Khuyến mãi",
        "pandq://orders" to "Đơn hàng",
        "pandq://cart" to "Giỏ hàng",
        "pandq://profile" to "Hồ sơ",
        "pandq://products" to "Sản phẩm"
    )

    // Show snackbar messages
    LaunchedEffect(uiState.successMessage, uiState.error) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Quản lý thông báo", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadTemplates() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Color(0xFF137fec)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.templates.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.templates.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Chưa có mẫu thông báo nào", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Nhấn + để tạo mới", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.templates) { template ->
                        NotificationTemplateItem(
                            template = template,
                            onToggle = { viewModel.toggleActive(template.id) },
                            onSend = { viewModel.sendNotification(template.id) },
                            onEdit = {
                                editingTemplateId = template.id
                                newTitle = template.title
                                newBody = template.body
                                selectedTargetUrl = template.targetUrl
                                // Parse scheduledAt to fill date/time if needed
                                if (template.scheduledAt != null) {
                                     try {
                                         // Expecting format "yyyy-MM-ddTHH:mm:ss"
                                         val parts = template.scheduledAt.split("T")
                                         if (parts.size >= 2) {
                                            selectedDate = parts[0]
                                            selectedTime = parts[1]
                                         }
                                     } catch (e: Exception) {
                                         // Ignore parse error
                                     }
                                }
                                showCreateDialog = true
                            },
                            onDelete = { viewModel.deleteTemplate(template.id) }
                        )
                    }
                }
            }

            // Loading overlay
            if (uiState.isLoading && uiState.templates.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }

    // Create Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { 
                showCreateDialog = false
                editingTemplateId = null
                newTitle = ""
                newBody = ""
                selectedDate = null
                selectedTime = null
                selectedTargetUrl = null
            },
            title = { Text(if (editingTemplateId == null) "Tạo mẫu thông báo" else "Cập nhật mẫu thông báo") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Tiêu đề") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newBody,
                        onValueChange = { newBody = it },
                        label = { Text("Nội dung") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                
                    // Scheduler UI
                    val context = androidx.compose.ui.platform.LocalContext.current
                    val calendar = java.util.Calendar.getInstance()

                    // Date DatePickerDialog
                    val datePickerDialog = android.app.DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                        },
                        calendar.get(java.util.Calendar.YEAR),
                        calendar.get(java.util.Calendar.MONTH),
                        calendar.get(java.util.Calendar.DAY_OF_MONTH)
                    )

                    // Time TimePickerDialog
                    val timePickerDialog = android.app.TimePickerDialog(
                        context,
                        { _, hourOfDay, minute ->
                            selectedTime = String.format("%02d:%02d:00", hourOfDay, minute)
                        },
                        calendar.get(java.util.Calendar.HOUR_OF_DAY),
                        calendar.get(java.util.Calendar.MINUTE),
                        true
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Lên lịch gửi (Không bắt buộc)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = selectedDate ?: "Chọn ngày")
                        }
                        
                        OutlinedButton(
                            onClick = { timePickerDialog.show() },
                            modifier = Modifier.weight(1f),
                            enabled = selectedDate != null
                        ) {
                            Text(text = selectedTime ?: "Chọn giờ")
                        }
                    }

                    if (selectedDate != null && selectedTime != null) {
                        Text(
                            text = "Dự kiến gửi: $selectedDate $selectedTime",
                            fontSize = 12.sp,
                            color = Color(0xFF059669),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    // Target URL Dropdown
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Điểm đến khi nhấn thông báo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ExposedDropdownMenuBox(
                        expanded = showTargetUrlDropdown,
                        onExpandedChange = { showTargetUrlDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = targetOptions.find { it.first == (selectedTargetUrl ?: "") }?.second ?: "Không chọn",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTargetUrlDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            singleLine = true
                        )
                        ExposedDropdownMenu(
                            expanded = showTargetUrlDropdown,
                            onDismissRequest = { showTargetUrlDropdown = false }
                        ) {
                            targetOptions.forEach { (url, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedTargetUrl = url.ifEmpty { null }
                                        showTargetUrlDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newBody.isNotBlank()) {
                            var scheduledAt: String? = null
                            if (selectedDate != null && selectedTime != null) {
                                scheduledAt = "${selectedDate}T${selectedTime}"
                            }
                            
                            if (editingTemplateId == null) {
                                viewModel.createTemplate(newTitle, newBody, scheduledAt, selectedTargetUrl)
                            } else {
                                viewModel.updateTemplate(editingTemplateId!!, newTitle, newBody, scheduledAt, selectedTargetUrl)
                            }
                            showCreateDialog = false
                            newTitle = ""
                            newBody = ""
                            selectedDate = null
                            selectedTime = null
                            selectedTargetUrl = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137fec))
                ) {
                    Text(if (editingTemplateId == null) "Tạo" else "Cập nhật")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showCreateDialog = false 
                    editingTemplateId = null
                    newTitle = ""
                    newBody = ""
                    selectedDate = null
                    selectedTime = null
                    selectedTargetUrl = null
                }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun NotificationTemplateItem(
    template: NotificationTemplateDto,
    onToggle: () -> Unit,
    onSend: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    // Type icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                when (template.type) {
                                    "PROMOTION" -> Color(0xFFFEF3C7)
                                    "ORDER_UPDATE" -> Color(0xFFDCFCE7)
                                    else -> Color(0xFFE0E7FF)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            when (template.type) {
                                "PROMOTION" -> Icons.Default.LocalOffer
                                "ORDER_UPDATE" -> Icons.Default.LocalShipping
                                else -> Icons.Default.Notifications
                            },
                            contentDescription = null,
                            tint = when (template.type) {
                                "PROMOTION" -> Color(0xFFD97706)
                                "ORDER_UPDATE" -> Color(0xFF059669)
                                else -> Color(0xFF4F46E5)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = template.title,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = template.body,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Toggle switch
                Switch(
                    checked = template.isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF10B981)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Surface(
                        color = if (template.isActive) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = if (template.isActive) "Đang hoạt động" else "Đã tắt",
                            color = if (template.isActive) Color(0xFF059669) else Color(0xFFDC2626),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Đã gửi ${template.sendCount} lần",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Row {
                    // Send button
                    IconButton(
                        onClick = onSend,
                        enabled = template.isActive
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (template.isActive) Color(0xFF137fec) else Color.Gray
                        )
                    }

                    // Edit button
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF137fec)
                        )
                    }

                    // Delete button
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFDC2626)
                        )
                    }
                }
            }
        }
    }
}
