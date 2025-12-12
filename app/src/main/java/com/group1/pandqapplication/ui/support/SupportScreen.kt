package com.group1.pandqapplication.ui.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.ui.theme.ProductPrimary
import com.group1.pandqapplication.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.ui.theme.RoleBackgroundLight
import com.group1.pandqapplication.ui.theme.SupportSurfaceDark
import com.group1.pandqapplication.ui.theme.SupportSurfaceLight
import com.group1.pandqapplication.ui.theme.SupportTextPrimaryDark
import com.group1.pandqapplication.ui.theme.SupportTextPrimaryLight
import com.group1.pandqapplication.ui.theme.SupportTextSecondaryDark
import com.group1.pandqapplication.ui.theme.SupportTextSecondaryLight

@Composable
fun SupportScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val surfaceColor = if (isDarkTheme) SupportSurfaceDark else SupportSurfaceLight
    val textPrimary = if (isDarkTheme) SupportTextPrimaryDark else SupportTextPrimaryLight
    val textSecondary = if (isDarkTheme) SupportTextSecondaryDark else SupportTextSecondaryLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = textPrimary)
                }
                Text(
                    "Hỗ trợ",
                    color = textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    style = androidx.compose.ui.text.TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            item {
                Box(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkTheme) SupportSurfaceDark else Color.Black.copy(alpha = 0.05f))
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = textSecondary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Tìm kiếm câu hỏi...",
                            color = textSecondary,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            
            // FAQ Section
            item {
                Text(
                    "Câu hỏi thường gặp",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }
            
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FAQItem("Tài khoản & Bảo mật", "This section contains answers to questions about login, password reset...", surfaceColor, textPrimary, textSecondary)
                    FAQItem("Thanh toán & Khuyến mãi", "This section contains answers to questions about payment methods...", surfaceColor, textPrimary, textSecondary)
                    FAQItem("Giao hàng & Vận chuyển", "This section contains answers to questions about shipping times...", surfaceColor, textPrimary, textSecondary)
                    FAQItem("Bảo hành & Đổi trả", "This section contains answers to questions about the return process...", surfaceColor, textPrimary, textSecondary)
                }
            }
            
            // Contact Section
            item {
                Text(
                    "Bạn cần thêm trợ giúp?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
                )
            }
            
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ContactItem(Icons.Default.ChatBubble, "Trò chuyện trực tiếp", "Get help right away", surfaceColor, textPrimary, textSecondary)
                    ContactItem(Icons.Default.Mail, "Gửi Email", "We'll get back to you soon", surfaceColor, textPrimary, textSecondary)
                    ContactItem(Icons.Default.Call, "Gọi tổng đài", "Available 8am - 10pm", surfaceColor, textPrimary, textSecondary)
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun FAQItem(
    title: String,
    content: String,
    surfaceColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(surfaceColor)
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Text(
                text = content,
                fontSize = 14.sp,
                color = textSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    surfaceColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(surfaceColor)
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(ProductPrimary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = ProductPrimary, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textPrimary)
            Text(subtitle, fontSize = 12.sp, color = textSecondary)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary)
    }
}

@Preview
@Composable
fun PreviewSupportScreen() {
    SupportScreen()
}
