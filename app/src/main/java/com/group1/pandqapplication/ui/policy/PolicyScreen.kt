package com.group1.pandqapplication.ui.policy

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.PolicyBorderDark
import com.group1.pandqapplication.shared.ui.theme.PolicyBorderLight
import com.group1.pandqapplication.shared.ui.theme.PolicyComponentDark
import com.group1.pandqapplication.shared.ui.theme.PolicyComponentLight
import com.group1.pandqapplication.shared.ui.theme.PolicySegmentDark
import com.group1.pandqapplication.shared.ui.theme.PolicySegmentLight
import com.group1.pandqapplication.shared.ui.theme.PolicyTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.PolicyTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.PolicyTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.PolicyTextSecondaryLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundLight

@Composable
fun PolicyScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val componentColor = if (isDarkTheme) PolicyComponentDark else PolicyComponentLight
    val segmentColor = if (isDarkTheme) PolicySegmentDark else PolicySegmentLight
    val borderColor = if (isDarkTheme) PolicyBorderDark else PolicyBorderLight
    val textPrimary = if (isDarkTheme) PolicyTextPrimaryDark else PolicyTextPrimaryLight
    val textSecondary = if (isDarkTheme) PolicyTextSecondaryDark else PolicyTextSecondaryLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(modifier = Modifier.background(componentColor).statusBarsPadding()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = ProductPrimary
                        )
                    }
                    Text(
                        text = "Chính sách & Điều khoản",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                HorizontalDivider(color = borderColor)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Segmented Control
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(componentColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(segmentColor)
                        .padding(4.dp),
                ) {
                    var selectedIndex by remember { mutableStateOf(0) }
                    val options = listOf("Chính sách Bảo mật", "Điều khoản Dịch vụ")
                    
                    options.forEachIndexed { index, text ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedIndex == index) ProductPrimary else Color.Transparent)
                                .clickable { selectedIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = text,
                                color = if (selectedIndex == index) Color.White else textPrimary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Meta Text
            Text(
                text = "Cập nhật lần cuối: 24 Tháng 10, 2024",
                fontSize = 14.sp,
                color = textSecondary,
                modifier = Modifier.padding(16.dp)
            )

            // Accordions
            Column(
                 modifier = Modifier
                     .padding(horizontal = 16.dp)
                     .clip(RoundedCornerShape(12.dp))
                     .background(componentColor)
            ) {
                AccordionItem(
                    title = "1. Giới thiệu & Phạm vi chính sách",
                    content = "Chúng tôi thu thập nhiều loại thông tin khác nhau liên quan đến các dịch vụ chúng tôi cung cấp...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "2. Thông tin chúng tôi thu thập",
                    content = "Thông tin chúng tôi thu thập có thể bao gồm, nhưng không giới hạn ở: dữ liệu cá nhân như tên, địa chỉ email...",
                    isOpenDefault = true,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "3. Cách chúng tôi sử dụng thông tin",
                    content = "Thông tin của bạn được sử dụng để xử lý đơn hàng, cá nhân hóa trải nghiệm của bạn...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "4. Chia sẻ & Tiết lộ thông tin",
                    content = "Chúng tôi có thể chia sẻ thông tin của bạn với các đối tác dịch vụ, chẳng hạn như các công ty vận chuyển...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "5. Biện pháp bảo mật dữ liệu",
                    content = "Chúng tôi thực hiện các biện pháp kỹ thuật và tổ chức để bảo vệ dữ liệu của bạn...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "6. Quyền & Lựa chọn của người dùng",
                    content = "Bạn có quyền truy cập, sửa đổi hoặc xóa thông tin cá nhân của mình...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = borderColor
                )
                AccordionItem(
                    title = "7. Thông tin liên hệ",
                    content = "Nếu bạn có bất kỳ câu hỏi nào về chính sách bảo mật này, vui lòng liên hệ với chúng tôi...",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    borderColor = Color.Transparent
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AccordionItem(
    title: String,
    content: String,
    isOpenDefault: Boolean = false,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(isOpenDefault) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary,
                modifier = Modifier.weight(1f)
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
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
        
        HorizontalDivider(color = borderColor)
    }
}


@Preview
@Composable
fun PreviewPolicyScreen() {
    PolicyScreen()
}
