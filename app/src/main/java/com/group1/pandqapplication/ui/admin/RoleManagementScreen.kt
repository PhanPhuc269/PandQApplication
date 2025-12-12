package com.group1.pandqapplication.ui.admin

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.ui.theme.ProductPrimary
import com.group1.pandqapplication.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.ui.theme.RoleBackgroundLight
import com.group1.pandqapplication.ui.theme.RoleSurfaceDark
import com.group1.pandqapplication.ui.theme.RoleSurfaceLight
import com.group1.pandqapplication.ui.theme.RoleTextPrimaryDark
import com.group1.pandqapplication.ui.theme.RoleTextPrimaryLight
import com.group1.pandqapplication.ui.theme.RoleTextSecondaryDark
import com.group1.pandqapplication.ui.theme.RoleTextSecondaryLight

@Composable
fun RoleManagementScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val surfaceColor = if (isDarkTheme) RoleSurfaceDark else RoleSurfaceLight
    val textPrimary = if (isDarkTheme) RoleTextPrimaryDark else RoleTextPrimaryLight
    val textSecondary = if (isDarkTheme) RoleTextSecondaryDark else RoleTextSecondaryLight
    val borderColor = if (isDarkTheme) Color(0xFF374151) else Color(0xFFE5E7EB)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = textPrimary
                        )
                    }
                    Text(
                        text = "Quản lý quyền truy cập",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = { /* Edit */ },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Edit",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProductPrimary
                        )
                    }
                }
                HorizontalDivider(color = borderColor)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Add Role */ },
                containerColor = ProductPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Role", modifier = Modifier.size(32.dp))
            }
        }
    ) { paddingValues ->
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
                    var searchText by remember { mutableStateOf("") }
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text(
                                    text = "Search for a role",
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


                item {
                    RoleItem(
                        icon = Icons.Default.Security,
                        title = "Quản trị viên",
                        count = "2 người dùng",
                        surfaceColor = surfaceColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                }
                item {
                    RoleItem(
                        icon = Icons.Default.ManageAccounts,
                        title = "Quản lý bán hàng",
                        count = "5 người dùng",
                        surfaceColor = surfaceColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                }
                item {
                    RoleItem(
                        icon = Icons.Default.Inventory2,
                        title = "Nhân viên kho",
                        count = "8 người dùng",
                        surfaceColor = surfaceColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                }
                item {
                    RoleItem(
                        icon = Icons.Default.SupportAgent,
                        title = "Hỗ trợ khách hàng",
                        count = "3 người dùng",
                        surfaceColor = surfaceColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = Color.Transparent // Last item no border
                    )
                }
            }
        }
    }
}

@Composable
fun RoleItem(
    icon: ImageVector,
    title: String,
    count: String,
    surfaceColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(horizontal = 16.dp, vertical = 12.dp), // min-h-[72px] ~ 12dp vertical padding
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
                tint = textSecondary.copy(alpha = 0.5f), // text-zinc-400
                modifier = Modifier.size(28.dp)
            )
        }
        HorizontalDivider(color = borderColor, modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Preview
@Composable
fun PreviewRoleManagementScreen() {
    RoleManagementScreen()
}
