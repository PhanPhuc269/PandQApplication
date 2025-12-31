package com.group1.pandqapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.AddProductBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.AddProductBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.AddProductError
import com.group1.pandqapplication.shared.ui.theme.AddProductPrimary
import com.group1.pandqapplication.shared.ui.theme.AddProductSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.AddProductSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.AddProductTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.AddProductTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.AddProductTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.AddProductTextSecondaryLight
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.ui.admin.AdminProductViewModel
import com.group1.pandqapplication.shared.data.remote.dto.ProductSpecificationDto
import androidx.compose.material3.CircularProgressIndicator
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddProductScreen(
    productId: String? = null,
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    viewModel: AdminProductViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Form State
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") } // Not used in API yet but UI has it
    var categoryId by remember { mutableStateOf("3fa85f64-5717-4562-b3fc-2c963f66afa6") } // Default fallback UUID
    
    // Determine mode
    val isEditMode = productId != null

    // Load product if edit mode
    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.getProduct(productId)
        }
    }

    // Bind data when product loads
    LaunchedEffect(uiState.currentProduct) {
        uiState.currentProduct?.let { product ->
            name = product.name
            description = product.description ?: ""
            price = product.price.toString()
            // costPrice is not in ProductDetailDto? Check backend. ProductDetailDto usually has it.
            // If missing in DTO, we skip.
            // costPrice = product.costPrice?.toString() ?: "" 
            categoryId = product.categoryId ?: categoryId
        }
    }

    // Handle Success
    LaunchedEffect(uiState.operationSuccess) {
        if (uiState.operationSuccess) {
            Toast.makeText(context, if (isEditMode) "Cập nhật thành công" else "Thêm thành công", Toast.LENGTH_SHORT).show()
            viewModel.clearOperationState()
            onBackClick() // Go back to list
        }
    }

    // Handle Error
    LaunchedEffect(uiState.operationError) {
        if (uiState.operationError != null) {
            Toast.makeText(context, uiState.operationError, Toast.LENGTH_SHORT).show()
            viewModel.clearOperationState()
        }
    }


    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) AddProductBackgroundDark else AddProductBackgroundLight
    val surfaceColor = if (isDarkTheme) AddProductSurfaceDark else AddProductSurfaceLight
    val textPrimary = if (isDarkTheme) AddProductTextPrimaryDark else AddProductTextPrimaryLight
    val textSecondary = if (isDarkTheme) AddProductTextSecondaryDark else AddProductTextSecondaryLight
    val inputBgColor = backgroundColor // Input uses background color inside surface

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(surfaceColor.copy(alpha = 0.8f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = textPrimary)
                }
                Text(
                    if (isEditMode) "Cập nhật sản phẩm" else "Thêm sản phẩm mới",

                    color = textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    style = androidx.compose.ui.text.TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                )
                Text(
                    "Huỷ",
                    color = AddProductPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onCancelClick() }
                )
            }
        },
        bottomBar = {
             Box(
                 modifier = Modifier
                     .fillMaxWidth()
                     .background(surfaceColor.copy(alpha = 0.8f))
                     .padding(16.dp)
             ) {
                 Button(
                     onClick = {},
                     modifier = Modifier.fillMaxWidth().height(48.dp),
                     shape = RoundedCornerShape(8.dp),
                     colors = ButtonDefaults.buttonColors(
                         containerColor = AddProductPrimary,
                         disabledContainerColor = AddProductPrimary.copy(alpha = 0.5f)
                     ),
                     enabled = false // Disabled as per design for now
                 ) {
                     Text("Lưu sản phẩm", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
            // Basic Info
            Section("Thông tin cơ bản", textPrimary) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LabelInput(
                        label = "Tên sản phẩm", 
                        placeholder = "Nhập tên sản phẩm", 
                        text = name,
                        onValueChange = { name = it },
                        labelColor = textSecondary, 
                        textColor = textPrimary, 
                        bgColor = inputBgColor
                    )
                    LabelInput(
                        label = "Mô tả chi tiết", 
                        placeholder = "Nhập mô tả chi tiết về sản phẩm", 
                        text = description,
                        onValueChange = { description = it },
                        labelColor = textSecondary, 
                        textColor = textPrimary, 
                        bgColor = inputBgColor, 
                        isMultiLine = true
                    )
                    
                    // Category Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(inputBgColor)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Widgets, null, tint = textSecondary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Danh mục", color = textPrimary, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Text("Chưa chọn", color = textSecondary, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForwardIos, null, tint = textSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }
            
            // Images & Video
            Section("Hình ảnh & Video", textPrimary) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text("Hình ảnh sản phẩm", color = textSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Add Image Placeholder
                             Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, AddProductPrimary.copy(alpha=0.5f), RoundedCornerShape(8.dp)) // Dashed border simulated or just solid alpha
                                    .background(inputBgColor)
                                    .clickable {},
                                contentAlignment = Alignment.Center
                             ) {
                                 Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                     Icon(Icons.Default.AddPhotoAlternate, null, tint = AddProductPrimary, modifier = Modifier.size(32.dp))
                                     Text("Thêm ảnh", fontSize = 12.sp, color = AddProductPrimary, fontWeight = FontWeight.Medium)
                                 }
                             }
                            // Existing Images
                             Box(modifier = Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(8.dp)).background(Color.Gray)) {
                                  AsyncImage(model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAMmUmyt0QrLiQq6UX_1MyoKlLGMWNI8ZVXZwjtkz8LazlpWIKYx_oDmjzRD_j7YmsMmr7r5OuoFEpxgW9oxZgjafuOmbFDkXsFzIiC4iwDqlpeblBL9JAY5gl5WJSAUY2oTPqWbnS4fUF_3f43lUr6V_-SrhAoL16EV9p49QGTbf9n3nhEXyDP_x2-_iLoHzd7y6xnK16C1ZUhj2-sr3G3-r0ldkk2Xif-iX8CaHt59jzQmzquIHUeJUbOY-GBrKBD5j8RqIn8MGE", contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                             }
                             Box(modifier = Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(8.dp)).background(Color.Gray)) {
                                  AsyncImage(model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAwFPVTdi3QP2hJ7WA_PAHNeNkzREnaQSVJJ6LjfryVIKKUWeB1C9g6qFbhWUPARag7chrHaSj_roKD8QqhrswVD3dyhI2nTdj7aFt_iG63FARo2RdPcVrrLNsMplUr_nfNl75Ex6lUKT8hhxQg_ZKcGtQPsY8_-OAdQuBssITPeAgekblgoh9hdlH92By7-c6wAG2w0MXP1aCNMh0bNt5HETVY6K5mSS3YcTh_p5rStLtfIe_dGytOOg2-DOXKexuyfNEdg5KU0_0", contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                             }
                        }
                    }
                    
                    Column {
                        Text("Video sản phẩm", color = textSecondary, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f/9f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, AddProductPrimary.copy(alpha=0.5f), RoundedCornerShape(8.dp))
                                .background(inputBgColor)
                                .clickable {},
                            contentAlignment = Alignment.Center
                        ) {
                             Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                 Icon(Icons.Default.VideoCall, null, tint = AddProductPrimary, modifier = Modifier.size(48.dp))
                                 Text("Thêm video", fontSize = 14.sp, color = AddProductPrimary, fontWeight = FontWeight.Medium)
                             }
                        }
                    }
                }
            }
            
            // Pricing & Inventory
            Section("Giá & Kho hàng", textPrimary) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        LabelInput(
                            label = "Giá bán", 
                            placeholder = "VD: 19990000", 
                            text = price,
                            onValueChange = { price = it },
                            labelColor = textSecondary, 
                            textColor = textPrimary, 
                            bgColor = inputBgColor, 
                            hasError = price.isNotEmpty() && price.toDoubleOrNull() == null
                        )
                        if (price.isEmpty()) {
                            Text("Vui lòng nhập giá bán", fontSize = 12.sp, color = AddProductError, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(modifier = Modifier.weight(1f)) { 
                            LabelInput(
                                label = "Giá vốn", 
                                placeholder = "Không bắt buộc", 
                                text = costPrice,
                                onValueChange = { costPrice = it },
                                labelColor = textSecondary, 
                                textColor = textPrimary, 
                                bgColor = inputBgColor
                            ) 
                        }
                        Box(modifier = Modifier.weight(1f)) { 
                            LabelInput(
                                label = "Tồn kho", 
                                placeholder = "VD: 100", 
                                text = stock,
                                onValueChange = { stock = it },
                                labelColor = textSecondary, 
                                textColor = textPrimary, 
                                bgColor = inputBgColor
                            ) 
                        }
                    }
                }
            }
            
            // Specifications
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                     Text("Thông số kỹ thuật", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                     Text("Thêm", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = AddProductPrimary)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(surfaceColor)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SpecRow("CPU", "Intel Core i9", textPrimary, inputBgColor, textSecondary)
                    SpecRow("RAM", "16GB DDR5", textPrimary, inputBgColor, textSecondary)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun Section(title: String, textColor: Color, content: @Composable () -> Unit) {
    Column {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor, modifier = Modifier.padding(bottom = 8.dp))
        content()
    }
}

@Composable
fun LabelInput(
    label: String,
    placeholder: String,
    text: String,
    onValueChange: (String) -> Unit,
    labelColor: Color,
    textColor: Color,
    bgColor: Color,
    isMultiLine: Boolean = false,
    hasError: Boolean = false
) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = labelColor, modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = labelColor.copy(alpha = 0.5f), fontSize = 16.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMultiLine) 120.dp else 48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(if (hasError) 1.dp else 0.dp, if (hasError) AddProductError else Color.Transparent, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = bgColor,
                unfocusedContainerColor = bgColor,
                focusedInputColor = textColor,
                unfocusedInputColor = textColor,
                focusedIndicatorColor = Color.Transparent, /* Hide underline */
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = !isMultiLine
        )
    }
}

@Composable
fun SpecRow(
    attr: String,
    value: String,
    textColor: Color,
    bgColor: Color,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(attr, color = textColor, fontSize = 16.sp)
        }
         Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(value, color = textColor, fontSize = 16.sp)
        }
        Icon(Icons.Default.Delete, null, tint = iconColor)
    }
}

@Preview
@Composable
fun PreviewAddProductScreen() {
    AddProductScreen()
}
