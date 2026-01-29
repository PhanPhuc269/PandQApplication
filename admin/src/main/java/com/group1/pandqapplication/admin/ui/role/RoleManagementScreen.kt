package com.group1.pandqapplication.admin.ui.role

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.RoleSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.RoleSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.RoleTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.RoleTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.RoleTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.RoleTextSecondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleManagementScreen(
    onBackClick: () -> Unit = {},
    onNavigateToAdminList: () -> Unit = {},
    viewModel: RoleViewModel = hiltViewModel()
) {
    val isDarkTheme = false
    
    val backgroundColor = Color(0xFFF8FAFC)
    val surfaceColor = Color(0xFFF8FAFC)
    val textPrimary = Color(0xFF1E293B)
    val textSecondary = Color(0xFF6B7280)
    val borderColor = Color(0xFFE5E7EB)
    
    val uiState by viewModel.uiState.collectAsState()
    val isCreating by viewModel.isCreating.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdEmail by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }
    
    // Handle create success
    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            showAddDialog = false
            showSuccessDialog = true
            viewModel.clearCreateSuccess()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Quản lý quyền truy cập") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadUsers() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ProductPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Admin", modifier = Modifier.size(32.dp))
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
                    CircularProgressIndicator(color = ProductPrimary)
                }
            }
            is RoleUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadUsers() }) {
                            Text("Thử lại")
                        }
                    }
                }
            }
            is RoleUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Search Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(surfaceColor),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = textSecondary
                                )
                            }
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (searchText.isEmpty()) {
                                        Text(
                                            text = "Tìm kiếm theo vai trò",
                                            color = textSecondary,
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }

                    // Role List
                    LazyColumn {
                        items(state.roles) { roleWithUsers ->
                            val icon = when (roleWithUsers.role) {
                                "ADMIN" -> Icons.Default.Security
                                else -> Icons.Default.ManageAccounts
                            }
                            RoleItem(
                                icon = icon,
                                title = roleWithUsers.displayName,
                                count = "${roleWithUsers.users.size} người dùng",
                                surfaceColor = surfaceColor,
                                textPrimary = textPrimary,
                                textSecondary = textSecondary,
                                borderColor = borderColor,
                                onClick = { onNavigateToAdminList() }
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
    
    // Success Dialog showing credentials
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Tạo tài khoản thành công") },
            text = {
                Column {
                    Text("📧 Email: $createdEmail", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "📬 Link đặt lại mật khẩu đã được gửi đến email trên.",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Admin mới cần kiểm tra email và click vào link để đặt mật khẩu. Nếu không tìm thất vui lòng kiểm tra hòm thư rác",
                        color = Color(0xFF6B7280),
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ProductPrimary)
                ) {
                    Text("Đã hiểu")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdminDialog(
    isLoading: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onConfirm: (email: String, fullName: String, role: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("ADMIN") }
    var expanded by remember { mutableStateOf(false) }
    
    val roles = listOf("ADMIN" to "Quản trị viên")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm người dùng mới") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Họ và tên") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = roles.find { it.first == selectedRole }?.second ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Vai trò") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        roles.forEach { (roleValue, roleLabel) ->
                            DropdownMenuItem(
                                text = { Text(roleLabel) },
                                onClick = {
                                    selectedRole = roleValue
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                
                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = ProductPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(email, fullName, selectedRole) },
                enabled = email.isNotBlank() && fullName.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = ProductPrimary)
            ) {
                Text("Thêm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun RoleItem(
    icon: ImageVector,
    title: String,
    count: String,
    surfaceColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    onClick: () -> Unit = {}
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(surfaceColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary
                )
                Text(
                    text = count,
                    fontSize = 14.sp,
                    color = textSecondary
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(28.dp)
            )
        }
        HorizontalDivider(color = borderColor, modifier = Modifier.padding(horizontal = 16.dp))
    }
}
