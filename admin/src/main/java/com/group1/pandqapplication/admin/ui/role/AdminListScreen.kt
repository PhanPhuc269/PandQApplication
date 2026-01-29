package com.group1.pandqapplication.admin.ui.role

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.AdminUserDto

// Color constants
private val BackgroundLight = Color(0xFFF8FAFC)
private val SurfaceLight = Color.White
private val TextPrimaryLight = Color(0xFF1F2937)
private val TextSecondaryLight = Color(0xFF6B7280)
private val BorderLight = Color(0xFFE5E7EB)
private val PrimaryColor = Color(0xFF10B981)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminListScreen(
    onBack: () -> Unit,
    viewModel: RoleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isCreating by viewModel.isCreating.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdEmail by remember { mutableStateOf("") }

    var showDemoteDialog by remember { mutableStateOf(false) }
    var selectedAdminToDemote by remember { mutableStateOf<AdminUserDto?>(null) }
    
    // Handle create success
    
    // Handle create success
    
    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = { Text("Danh sách Quản trị viên") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadUsers() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceLight
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Admin")
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is RoleUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryColor)
                }
            }
            is RoleUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = Color.Red
                    )
                }
            }
            is RoleUiState.Success -> {
                val adminUsers = state.roles.firstOrNull()?.users ?: emptyList()
                
                if (adminUsers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có admin nào",
                            color = TextSecondaryLight
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(top = 8.dp)
                    ) {
                        items(adminUsers) { user ->
                            AdminUserItem(
                                user = user,
                                surfaceColor = SurfaceLight,
                                textPrimary = TextPrimaryLight,
                                textSecondary = TextSecondaryLight,
                                borderColor = BorderLight,
                                onDemoteClick = {
                                    selectedAdminToDemote = user
                                    showDemoteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Add Admin Dialog
    if (showAddDialog) {
        AddAdminDialog(
            isLoading = isCreating,
            errorMessage = errorMessage,
            onDismiss = {
                showAddDialog = false
                viewModel.clearError()
            },
            onConfirm = { email, fullName, role ->
                createdEmail = email
                viewModel.createAdminUser(email, fullName, role)
            }
        )
    }
    
    // Success Dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Tạo tài khoản thành công") },
            text = {
                Column {
                    Text("Email: $createdEmail", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Link đặt lại mật khẩu đã được gửi đến email trên.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Admin mới cần kiểm tra email và click vào link để đặt mật khẩu. Nếu không tìm thấy vui lòng kiểm tra hòm thư rác",
                        color = TextSecondaryLight,
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Text("Đã hiểu")
                }
            }
        )
    }

    // Demote Confirmation Dialog
    if (showDemoteDialog && selectedAdminToDemote != null) {
        AlertDialog(
            onDismissRequest = { showDemoteDialog = false },
            title = { Text("Hủy quyền Admin") },
            text = {
                Text("Bạn có chắc chắn muốn hủy quyền Admin của ${selectedAdminToDemote?.fullName} (${selectedAdminToDemote?.email})? \n\nTài khoản này sẽ trở về quyền Khách hàng (Customer).")
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedAdminToDemote?.let { viewModel.demoteUser(it.email) }
                        showDemoteDialog = false
                        selectedAdminToDemote = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Hủy quyền")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDemoteDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Composable
fun AdminUserItem(
    user: AdminUserDto,
    surfaceColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    onDemoteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.fullName?.firstOrNull()?.uppercase() ?: "?",
                    color = PrimaryColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName ?: "Chưa có tên",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = textSecondary
                )
            }
            // Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (user.status == "ACTIVE") Color(0xFF10B981).copy(alpha = 0.1f)
                            else Color(0xFFEF4444).copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (user.status == "ACTIVE") "Hoạt động" else "Đã khóa",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (user.status == "ACTIVE") Color(0xFF10B981) else Color(0xFFEF4444)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Allow removing ONLY if it's not the seeded admin or self (simple check for now)
                IconButton(onClick = onDemoteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Admin",
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}
