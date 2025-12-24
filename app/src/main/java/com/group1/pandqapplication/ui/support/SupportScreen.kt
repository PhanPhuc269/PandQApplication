package com.group1.pandqapplication.ui.support

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.BackgroundDark
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight
import com.group1.pandqapplication.shared.ui.theme.CardDark
import com.group1.pandqapplication.shared.ui.theme.CardLight
import com.group1.pandqapplication.shared.ui.theme.Primary
import com.group1.pandqapplication.shared.ui.theme.TextDarkPrimary
import com.group1.pandqapplication.shared.ui.theme.TextDarkSecondary
import com.group1.pandqapplication.shared.ui.theme.TextLightPrimary
import com.group1.pandqapplication.shared.ui.theme.TextLightSecondary

data class FAQItem(
    val question: String,
    val answer: String
)

data class FAQCategory(
    val title: String,
    val items: List<FAQItem>
)

@Composable
fun SupportScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    val backgroundColor = if (isDarkTheme) BackgroundDark else BackgroundLight
    val cardColor = if (isDarkTheme) CardDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSecondary = if (isDarkTheme) TextDarkSecondary else TextLightSecondary

    val faqCategories = listOf(
        FAQCategory(
            title = "Tài khoản & Bảo mật",
            items = listOf(
                FAQItem(
                    "Làm sao tôi có thể thay đổi mật khẩu?",
                    "Bạn có thể thay đổi mật khẩu bằng cách vào phần Cài đặt > Bảo mật > Thay đổi mật khẩu."
                ),
                FAQItem(
                    "Tài khoản của tôi bị khóa, phải làm sao?",
                    "Liên hệ với bộ phận hỗ trợ qua email hoặc chat để được giải quyết trong 24 giờ."
                ),
                FAQItem(
                    "Làm sao để bảo vệ tài khoản của tôi?",
                    "Sử dụng mật khẩu mạnh, bật xác thực hai yếu tố, và không chia sẻ thông tin cá nhân."
                )
            )
        ),
        FAQCategory(
            title = "Thanh toán & Khuyến mãi",
            items = listOf(
                FAQItem(
                    "Có những phương thức thanh toán nào?",
                    "Chúng tôi hỗ trợ thanh toán khi nhận hàng (COD), thẻ tín dụng, chuyển khoản ngân hàng, và ví điện tử."
                ),
                FAQItem(
                    "Làm sao sử dụng mã giảm giá?",
                    "Nhập mã giảm giá trong giỏ hàng trước khi thanh toán. Mã sẽ được tự động áp dụng."
                ),
                FAQItem(
                    "Tôi có thể hoàn lại tiền không?",
                    "Có, bạn có thể hoàn lại tiền nếu sản phẩm bị lỗi hoặc khác so với mô tả. Liên hệ hỗ trợ trong 7 ngày."
                )
            )
        ),
        FAQCategory(
            title = "Giao hàng & Vận chuyển",
            items = listOf(
                FAQItem(
                    "Thời gian giao hàng bao lâu?",
                    "Thời gian giao hàng thường là 2-5 ngày tùy vào vị trí của bạn."
                ),
                FAQItem(
                    "Làm sao để theo dõi đơn hàng?",
                    "Bạn có thể theo dõi đơn hàng trong phần 'Đơn hàng của tôi' bằng cách nhấn vào từng đơn hàng."
                ),
                FAQItem(
                    "Tôi có thể thay đổi địa chỉ giao hàng không?",
                    "Bạn có thể thay đổi địa chỉ nếu đơn hàng chưa được giao. Liên hệ hỗ trợ ngay."
                )
            )
        ),
        FAQCategory(
            title = "Bảo hành & Đổi trả",
            items = listOf(
                FAQItem(
                    "Chính sách bảo hành là gì?",
                    "Sản phẩm được bảo hành theo chính sách của nhà sản xuất từ 1-24 tháng."
                ),
                FAQItem(
                    "Làm sao để đổi sản phẩm lỗi?",
                    "Liên hệ hỗ trợ với hình ảnh sản phẩm lỗi. Chúng tôi sẽ sắp xếp đổi hoặc hoàn tiền."
                ),
                FAQItem(
                    "Thời hạn đổi hàng là bao lâu?",
                    "Bạn có 30 ngày kể từ ngày nhận hàng để yêu cầu đổi hàng lỗi."
                )
            )
        )
    )

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Primary
                    )
                }
                Text(
                    text = "Hỗ trợ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = {
                    Text(
                        "Tìm kiếm câu hỏi...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = cardColor,
                    unfocusedContainerColor = cardColor,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.2f)
                ),
                singleLine = true
            )

            // FAQ Categories
            Text(
                text = "Câu hỏi thường gặp",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            faqCategories.forEach { category ->
                FAQCategoryCard(
                    category = category,
                    cardColor = cardColor,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }

            // Contact Support Section
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Bạn cần thêm trợ giúp?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Live Chat
            ContactOption(
                icon = Icons.Default.ChatBubble,
                title = "Trò chuyện trực tiếp",
                description = "Get help right away",
                cardColor = cardColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onClick = { /* Open Live Chat */ }
            )

            // Email Support
            ContactOption(
                icon = Icons.Default.Email,
                title = "Gửi Email",
                description = "We'll get back to you soon",
                cardColor = cardColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onClick = { /* Open Email */ }
            )

            // Phone Support
            ContactOption(
                icon = Icons.Default.Phone,
                title = "Gọi tổng đài hỗ trợ",
                description = "Available 8am - 10pm",
                cardColor = cardColor,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                onClick = { /* Open Phone Dialer */ }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FAQCategoryCard(
    category: FAQCategory,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Primary,
                    modifier = Modifier.rotate(if (isExpanded) 180f else 0f)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.2f))
                    
                    category.items.forEach { item ->
                        FAQItemContent(
                            question = item.question,
                            answer = item.answer,
                            textPrimary = textPrimary,
                            textSecondary = textSecondary
                        )
                        HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun FAQItemContent(
    question: String,
    answer: String,
    textPrimary: Color,
    textSecondary: Color
) {
    var isAnswerExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isAnswerExpanded = !isAnswerExpanded }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = question,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = "Expand Answer",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(if (isAnswerExpanded) 180f else 0f)
            )
        }

        AnimatedVisibility(
            visible = isAnswerExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    fontSize = 13.sp,
                    color = textSecondary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ContactOption(
    icon: ImageVector,
    title: String,
    description: String,
    cardColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(color = Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = textSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Open",
                tint = Primary,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(-90f)
            )
        }
    }
}
