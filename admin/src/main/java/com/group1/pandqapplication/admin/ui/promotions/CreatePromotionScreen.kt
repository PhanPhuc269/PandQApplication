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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.KeyboardType
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.data.remote.dto.DiscountType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePromotionScreen(
    promotionId: String? = null,
    onBackClick: () -> Unit = {},
    viewModel: PromotionViewModel = hiltViewModel()
) {
    val createState by viewModel.createState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isDarkTheme = false

    val backgroundColor = if (isDarkTheme) PromotionBackgroundDark else PromotionBackgroundLight
    val cardColor = if (isDarkTheme) PromotionCardDark else PromotionCardLight
    val textPrimary = if (isDarkTheme) PromotionTextPrimaryDark else PromotionTextPrimaryLight
    val textSecondary = if (isDarkTheme) PromotionTextSecondaryDark else PromotionTextSecondaryLight
    val borderColor = if (isDarkTheme) PromotionBorderDark else PromotionBorderLight

    // Load promotion if editing
    LaunchedEffect(promotionId) {
        if (promotionId != null) {
            viewModel.loadPromotionById(promotionId)
        } else {
            viewModel.resetCreateState()
        }
    }

    // Handle success - navigate back
    LaunchedEffect(createState.isSuccess) {
        if (createState.isSuccess) {
            val message = if (createState.isEditMode) "Cập nhật khuyến mãi thành công!" else "Tạo khuyến mãi thành công!"
            snackbarHostState.showSnackbar(message)
            viewModel.resetCreateState()
            onBackClick()
        }
    }

    // Show error
    LaunchedEffect(createState.error) {
        createState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
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
                        text = if (createState.isEditMode) "Chỉnh sửa khuyến mãi" else "Tạo khuyến mãi",
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
                    onClick = { viewModel.savePromotion() },
                    enabled = !createState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(top = 1.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PromotionPrimary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (createState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (createState.isEditMode) "Cập nhật" else "Lưu khuyến mãi",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
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
                InputRowWithState(
                    label = "Tên khuyến mãi",
                    placeholder = "VD: Giảm giá Black Friday",
                    value = createState.name,
                    onValueChange = { viewModel.updateName(it) },
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    showDivider = true,
                    borderColor = borderColor
                )
                // Code
                InputRowWithState(
                    label = "Mã khuyến mãi",
                    placeholder = "VD: BLACKFRIDAY2024",
                    value = createState.code,
                    onValueChange = { viewModel.updateCode(it) },
                    textPrimary = textPrimary,
                    textSecondary = textSecondary,
                    showDivider = true,
                    borderColor = borderColor
                )
                // Description
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
                        value = createState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = textPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (createState.description.isEmpty()) {
                                    Text("Nhập mô tả ngắn gọn", color = textSecondary.copy(alpha = 0.5f))
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // Type & Value
            SectionTitle(text = "Loại & Giá trị", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                // Segmented Control - connected to ViewModel
                val selectedTypeIndex = when (createState.type) {
                    DiscountType.PERCENTAGE -> 0
                    DiscountType.FIXED_AMOUNT -> 1
                    DiscountType.FREE_SHIPPING -> 2
                }
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
                                .height(44.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedTypeIndex == index) PromotionPrimary else Color.Transparent)
                                .clickable { 
                                    val type = when (index) {
                                        0 -> DiscountType.PERCENTAGE
                                        1 -> DiscountType.FIXED_AMOUNT
                                        else -> DiscountType.FREE_SHIPPING
                                    }
                                    viewModel.updateType(type)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTypeIndex == index) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (selectedTypeIndex == index) Color.White else PromotionPrimary,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
                
                HorizontalDivider(color = borderColor)
                
                // Value Input - connected to ViewModel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (createState.type) {
                            DiscountType.FREE_SHIPPING -> "Giảm phí vận chuyển"
                            else -> "Giá trị giảm"
                        },
                        fontSize = 16.sp,
                        color = textPrimary
                    )
                    
                    Row(
                        modifier = Modifier
                            .width(120.dp)
                            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                            .background(backgroundColor, RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = createState.value,
                            onValueChange = { viewModel.updateValue(it) },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = textPrimary,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.CenterEnd) {
                                    if (createState.value.isEmpty()) {
                                        Text(
                                            when (createState.type) {
                                                DiscountType.PERCENTAGE -> "20"
                                                DiscountType.FREE_SHIPPING -> "100"
                                                else -> "50000"
                                            }, 
                                            color = textSecondary.copy(alpha = 0.5f)
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when (createState.type) {
                                DiscountType.FIXED_AMOUNT -> "đ"
                                else -> "%" // PERCENTAGE and FREE_SHIPPING use %
                            },
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
                     showDivider = true,
                     showArrow = false
                 )
                 
                 // Min Order Value - connected to ViewModel
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
                            value = createState.minOrderValue,
                            onValueChange = { viewModel.updateMinOrderValue(it) },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = textPrimary,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            decorationBox = { innerTextField ->
                                Box(contentAlignment = Alignment.CenterEnd) {
                                    if (createState.minOrderValue.isEmpty()) {
                                        Text("100,000", color = textSecondary.copy(alpha = 0.5f))
                                    }
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
                     showDivider = true,
                     showArrow = false
                 )
                 
                 // Quantity Limit - Số lượng voucher
                 var isUnlimited by remember { mutableStateOf(createState.quantityLimit.isEmpty()) }
                 
                 Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Số lượng voucher",
                        fontSize = 16.sp,
                        color = textPrimary
                    )
                    
                    if (isUnlimited) {
                        // Show "Không giới hạn" with click to change
                        Row(
                            modifier = Modifier
                                .clickable { isUnlimited = false }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Không giới hạn",
                                fontSize = 16.sp,
                                color = PromotionPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = null,
                                tint = textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    } else {
                        // Show input field for quantity
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(100.dp)
                                    .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                                    .background(backgroundColor, RoundedCornerShape(6.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BasicTextField(
                                    value = createState.quantityLimit,
                                    onValueChange = { viewModel.updateQuantityLimit(it) },
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = textPrimary,
                                        textAlign = TextAlign.End
                                    ),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    decorationBox = { innerTextField ->
                                        Box(contentAlignment = Alignment.CenterEnd) {
                                            if (createState.quantityLimit.isEmpty()) {
                                                Text("100", color = textSecondary.copy(alpha = 0.5f))
                                            }
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Button to switch to unlimited
                            Text(
                                text = "∞",
                                fontSize = 20.sp,
                                color = PromotionPrimary,
                                modifier = Modifier
                                    .clickable {
                                        isUnlimited = true
                                        viewModel.updateQuantityLimit("")
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
            
            // Effective Duration
            SectionTitle(text = "Thời gian hiệu lực", textColor = textPrimary)
            CardContainer(cardColor = cardColor, borderColor = borderColor) {
                 // Date picker states
                 var showStartDatePicker by remember { mutableStateOf(false) }
                 var showEndDatePicker by remember { mutableStateOf(false) }
                 
                 val today = java.time.LocalDate.now()
                 val defaultStartDate = java.time.LocalDateTime.now()
                 val defaultEndDate = java.time.LocalDateTime.now().plusDays(30)
                 
                 // Start date - default to now
                 val startDateDisplay = if (createState.startDate.isBlank()) {
                     defaultStartDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"))
                 } else {
                     try {
                         java.time.LocalDateTime.parse(createState.startDate).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"))
                     } catch (e: Exception) { createState.startDate }
                 }
                 
                 Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartDatePicker = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                     Text("Ngày bắt đầu", fontSize = 16.sp, color = textPrimary)
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Text(startDateDisplay, fontSize = 16.sp, color = PromotionPrimary)
                         Spacer(modifier = Modifier.width(4.dp))
                         Icon(
                             imageVector = Icons.Default.ArrowForwardIos,
                             contentDescription = null,
                             tint = textSecondary,
                             modifier = Modifier.size(14.dp)
                         )
                     }
                 }
                 HorizontalDivider(color = borderColor)
                 
                 // End date - default to 30 days from now
                 val endDateDisplay = if (createState.endDate.isBlank()) {
                     defaultEndDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"))
                 } else {
                     try {
                         java.time.LocalDateTime.parse(createState.endDate).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"))
                     } catch (e: Exception) { createState.endDate }
                 }
                 
                 Row(
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndDatePicker = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                 ) {
                     Text("Ngày kết thúc", fontSize = 16.sp, color = textPrimary)
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Text(endDateDisplay, fontSize = 16.sp, color = PromotionPrimary)
                         Spacer(modifier = Modifier.width(4.dp))
                         Icon(
                             imageVector = Icons.Default.ArrowForwardIos,
                             contentDescription = null,
                             tint = textSecondary,
                             modifier = Modifier.size(14.dp)
                         )
                     }
                 }
                 
                 // Start Date Picker Dialog
                 if (showStartDatePicker) {
                     DatePickerDialog(
                         onDismissRequest = { showStartDatePicker = false },
                         confirmButton = {
                             androidx.compose.material3.TextButton(
                                 onClick = { showStartDatePicker = false }
                             ) {
                                 Text("Xác nhận", color = PromotionPrimary)
                             }
                         },
                         dismissButton = {
                             androidx.compose.material3.TextButton(
                                 onClick = { showStartDatePicker = false }
                             ) {
                                 Text("Hủy", color = textSecondary)
                             }
                         }
                     ) {
                         val datePickerState = rememberDatePickerState(
                             initialSelectedDateMillis = System.currentTimeMillis(),
                             selectableDates = object : SelectableDates {
                                 override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                     // Start date must be >= today
                                     return utcTimeMillis >= today.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                                 }
                             }
                         )
                         
                         DatePicker(state = datePickerState)
                         
                         LaunchedEffect(datePickerState.selectedDateMillis) {
                             datePickerState.selectedDateMillis?.let { millis ->
                                 val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                     .atZone(java.time.ZoneId.systemDefault())
                                     .toLocalDateTime()
                                 viewModel.updateStartDate(selectedDate.toString())
                             }
                         }
                     }
                 }
                 
                 // End Date Picker Dialog
                 if (showEndDatePicker) {
                     val minEndDate = if (createState.startDate.isBlank()) {
                         today
                     } else {
                         try {
                             java.time.LocalDateTime.parse(createState.startDate).toLocalDate()
                         } catch (e: Exception) { today }
                     }
                     
                     DatePickerDialog(
                         onDismissRequest = { showEndDatePicker = false },
                         confirmButton = {
                             androidx.compose.material3.TextButton(
                                 onClick = { showEndDatePicker = false }
                             ) {
                                 Text("Xác nhận", color = PromotionPrimary)
                             }
                         },
                         dismissButton = {
                             androidx.compose.material3.TextButton(
                                 onClick = { showEndDatePicker = false }
                             ) {
                                 Text("Hủy", color = textSecondary)
                             }
                         }
                     ) {
                         val endDatePickerState = rememberDatePickerState(
                             initialSelectedDateMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                             selectableDates = object : SelectableDates {
                                 override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                     // End date must be >= start date (or today if no start date)
                                     return utcTimeMillis >= minEndDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                                 }
                             }
                         )
                         
                         DatePicker(state = endDatePickerState)
                         
                         LaunchedEffect(endDatePickerState.selectedDateMillis) {
                             endDatePickerState.selectedDateMillis?.let { millis ->
                                 val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                     .atZone(java.time.ZoneId.systemDefault())
                                     .toLocalDateTime()
                                     .withHour(23).withMinute(59) // End of day
                                 viewModel.updateEndDate(selectedDate.toString())
                             }
                         }
                     }
                 }
            }
            
            // Active Toggle - connected to ViewModel
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
                        checked = createState.isActive, 
                        onCheckedChange = { viewModel.updateIsActive(it) },
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
    showDivider: Boolean,
    showArrow: Boolean = true
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
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
    if (showDivider) {
        HorizontalDivider(color = borderColor)
    }
}

@Composable
fun InputRowWithState(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
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
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = textPrimary,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(placeholder, color = textSecondary.copy(alpha = 0.5f))
                    }
                    innerTextField()
                }
            }
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
