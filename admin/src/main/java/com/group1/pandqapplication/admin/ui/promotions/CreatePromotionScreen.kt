package com.group1.pandqapplication.admin.ui.promotions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.PromotionBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.PromotionBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.PromotionBorderDark
import com.group1.pandqapplication.shared.ui.theme.PromotionBorderLight
import com.group1.pandqapplication.shared.ui.theme.PromotionCardDark
import com.group1.pandqapplication.shared.ui.theme.PromotionCardLight
import com.group1.pandqapplication.shared.ui.theme.PromotionPrimary
import com.group1.pandqapplication.shared.ui.theme.PromotionTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.PromotionTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.PromotionTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.PromotionTextSecondaryLight

@Composable
fun CreatePromotionScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false

    val backgroundColor = if (isDarkTheme) PromotionBackgroundDark else PromotionBackgroundLight
    val cardColor = if (isDarkTheme) PromotionCardDark else PromotionCardLight
    val textPrimary = if (isDarkTheme) PromotionTextPrimaryDark else PromotionTextPrimaryLight
    val textSecondary = if (isDarkTheme) PromotionTextSecondaryDark else PromotionTextSecondaryLight
    val borderColor = if (isDarkTheme) PromotionBorderDark else PromotionBorderLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.8f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = PromotionPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "Tạo khuyến mãi",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                HorizontalDivider(color = borderColor)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor.copy(alpha = 0.8f))
                    .padding(16.dp)
            ) {
                // Border top logic handled by box or divider above? Design asks for border-t.
                HorizontalDivider(
                    modifier = Modifier.align(Alignment.TopCenter), // Not quite right, better put divider before box
                    color = borderColor
                )
                // Just styling the button
                Button(
                    onClick = { /* Save */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(top = 1.dp), // Space for divider line
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PromotionPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Lưu khuyến mãi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // General Information
            SectionTitle(text = "Thông tin chung", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                // Name
                InputRow(
                    label = "Tên khuyến mãi",
                    placeholder = "VD: Giảm giá Black Friday",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    showDivider = true,
                    borderColor = borderColor
                )
                // Code
                InputRow(
                    label = "Mã khuyến mãi",
                    placeholder = "VD: BLACKFRIDAY2024",
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    showDivider = true,
                    borderColor = borderColor
                )
                // Description needs to be TextArea
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Mô tả (Tùy chọn)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    BasicTextField(
                        value = "",
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = textPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
                        decorationBox = { innerTextField ->
                            if (false) { // Placeholder logic simply
                                Text("Nhập mô tả ngắn gọn", color = textSecondary)
                            } else {
                                Box {
                                    if(true) Text("Nhập mô tả ngắn gọn", color = textSecondary) // Hack for empty check
                                    innerTextField()
                                }
                            }
                        }
                    )
                }
            }

            // Type & Value
            SectionTitle(text = "Loại & Giá trị", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                // Segmented Control
                var selectedTypeIndex by remember { mutableStateOf(0) }
                val types = listOf("Phần trăm", "Số tiền cố định", "Miễn phí vận chuyển")
                
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(backgroundColor, RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    types.forEachIndexed { index, title ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedTypeIndex == index) PromotionPrimary else Color.Transparent)
                                .clickable { selectedTypeIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 12.sp, // Adjusted to fit
                                fontWeight = if (selectedTypeIndex == index) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (selectedTypeIndex == index) Color.White else PromotionPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                HorizontalDivider(color = borderColor)
                
                // Value Input
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Giá trị giảm",
                        fontSize = 16.sp,
                        color = textPrimary
                    )
                    
                    Row(
                        modifier = Modifier
                            .width(100.dp)
                            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                            .background(backgroundColor, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = "",
                            onValueChange = {},
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = textPrimary,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.CenterEnd) {
                                    if(true) Text("20", color = textSecondary.copy(alpha = 0.5f))
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%", // Dynamic based on type? Design shows % hardcoded in screenshot context
                            color = textSecondary
                        )
                    }
                }
            }
            
            // Application Conditions
            SectionTitle(text = "Điều kiện áp dụng", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                 NavRow(
                     label = "Áp dụng cho",
                     value = "Tất cả sản phẩm",
                     textPrimary = textPrimary,
                     textSecondary = textSecondary,
                     borderColor = borderColor,
                     showDivider = true
                 )
                 
                 // Min Order Value
                 Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Giá trị đơn hàng tối thiểu",
                        fontSize = 16.sp,
                        color = textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Row(
                        modifier = Modifier
                            .width(130.dp)
                            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                            .background(backgroundColor, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = "",
                            onValueChange = {},
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = textPrimary,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.CenterEnd) {
                                    if(true) Text("500,000", color = textSecondary.copy(alpha = 0.5f))
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "đ", color = textSecondary)
                    }
                }
                HorizontalDivider(color = borderColor)
                
                NavRow(
                     label = "Đối tượng khách hàng",
                     value = "Tất cả",
                     textPrimary = textPrimary,
                     textSecondary = textSecondary,
                     borderColor = borderColor,
                     showDivider = false
                 )
            }
            
            // Effective Duration
            SectionTitle(text = "Thời gian hiệu lực", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                 Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                     Text("Ngày bắt đầu", fontSize = 16.sp, color = textPrimary)
                     Text("10/11/2024, 09:00", fontSize = 16.sp, color = textSecondary)
                 }
                 HorizontalDivider(color = borderColor)
                 Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                     Text("Ngày kết thúc", fontSize = 16.sp, color = textPrimary)
                     Text("15/11/2024, 23:59", fontSize = 16.sp, color = textSecondary)
                 }
            }
            
            // Active Toggle
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                Row(
                   modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Kích hoạt ngay", fontSize = 16.sp, color = textPrimary)
                    Switch(
                        checked = true, 
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PromotionPrimary,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD1D5DB)
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(60.dp)) // Padding for bottom bar
        }
    }
}

@Composable
fun SectionTitle(text: String, textColor: Color) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun CardContainer(
    cardColor: Color,
    borderColor: Color,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
    ) {
        content()
    }
}

@Composable
fun InputRow(
    label: String,
    placeholder: String,
    textPrimary: Color,
    textSecondary: Color,
    showDivider: Boolean,
    borderColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textSecondary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        BasicTextField(
            value = "",
            onValueChange = {},
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = textPrimary,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if(true) Text(placeholder, color = textSecondary.copy(alpha = 0.5f))
                    innerTextField()
                }
            }
        )
    }
    if (showDivider) {
        HorizontalDivider(color = borderColor)
    }
}

@Composable
fun NavRow(
    label: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color,
    showDivider: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = textPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = textSecondary,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = null,
            tint = textSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
    if (showDivider) {
        HorizontalDivider(color = borderColor)
    }
}

@Preview
@Composable
fun PreviewCreatePromotionScreen() {
    CreatePromotionScreen()
}
