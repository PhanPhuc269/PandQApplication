package com.group1.pandqapplication.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit
) {
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) {
                         Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = Color(0xFF1F2937))
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                         Text(
                             "Hồ sơ cá nhân", 
                             fontWeight = FontWeight.Bold, 
                             fontSize = 18.sp, 
                             color = Color(0xFF111827)
                         ) 
                    }
                    Spacer(modifier = Modifier.size(48.dp)) // Balance the Back button size
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                 AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCwILGFmyQOJSWfUNVKWm8accZ99ZMUMajjM5_16dG6r-LbAS5VUTS-SjVchAwiv6T8CUIJPoO9_QIWhQbbjUv2YwJGkrHinCulA75-HDAzfS3IBlDyl5IWJfgMBTJLdOWCV4MuPguh-U1AvZ6LUb_qHUUy_V6357n6jLUAKXeojajAMcxJvqXZmY2bAsliPryIr-4ny0TgV6__D9tF8IEvF4sKVH2DSX1u2kUrTQSdURvOV2aJn__I-w84iZWceHB4qacbhicBJQg",
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Lê Minh Anh", 
                    fontSize = 22.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF111827)
                )
                Text(
                    "leminhanh@example.com", 
                    fontSize = 16.sp, 
                    color = Color(0xFF6B7280)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .widthIn(min = 200.dp)
                ) {
                    Text("Edit Profile", fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Account Section
            SectionHeader(title = "Tài khoản")
            SectionContainer {
                SectionItem(icon = Icons.Outlined.Person, label = "Thông tin cá nhân", primaryColor = primaryColor)
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(icon = Icons.Outlined.Home, label = "Sổ địa chỉ", primaryColor = primaryColor)
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(icon = Icons.Outlined.ReceiptLong, label = "Lịch sử đơn hàng", primaryColor = primaryColor)
            }
            
            // Settings Section
            SectionHeader(title = "Cài đặt")
            SectionContainer {
                SectionItem(icon = Icons.Outlined.Notifications, label = "Cài đặt thông báo", primaryColor = primaryColor)
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(icon = Icons.Outlined.Settings, label = "Cài đặt ứng dụng", primaryColor = primaryColor)
            }
            
            // Support Section
            Spacer(modifier = Modifier.height(20.dp))
            SectionContainer {
                SectionItem(icon = Icons.Outlined.HelpOutline, label = "Hỗ trợ", primaryColor = primaryColor)
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Filled.Logout, 
                    label = "Đăng xuất", 
                    isDestructive = true, 
                    primaryColor = primaryColor,
                    onClick = onLogout
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SectionContainer(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = Color.White.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun SectionItem(
    icon: ImageVector, 
    label: String, 
    primaryColor: Color, 
    isDestructive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val contentColor = if (isDestructive) Color.Red else Color(0xFF1F2937)
    val iconBgColor = if (isDestructive) Color.Red.copy(alpha = 0.1f) else primaryColor.copy(alpha = 0.1f)
    val iconTint = if (isDestructive) Color.Red else primaryColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
         Box(
             modifier = Modifier
                 .size(40.dp)
                 .clip(RoundedCornerShape(8.dp))
                 .background(iconBgColor),
             contentAlignment = Alignment.Center
         ) {
             Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
         }
         Text(
             text = label,
             modifier = Modifier
                 .weight(1f)
                 .padding(start = 16.dp),
             fontSize = 16.sp,
             color = contentColor
         )
         if (!isDestructive) {
             Icon(
                 Icons.Filled.ChevronRight, 
                 contentDescription = null, 
                 tint = Color(0xFF9CA3AF)
             )
         }
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AccountScreen(onLogout = {})
}
