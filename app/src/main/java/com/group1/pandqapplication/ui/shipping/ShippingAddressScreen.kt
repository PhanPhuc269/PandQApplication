package com.group1.pandqapplication.ui.shipping

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.shared.data.remote.dto.AddressDto
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShippingAddressScreen(
    onBackClick: () -> Unit,
    userId: String = "",  // Should be passed from previous screen or retrieved from SharedPreferences
    viewModel: ShippingAddressViewModel = hiltViewModel()
) {
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = Color(0xFFF8F6F6)
    val surfaceColor = Color.White
    
    val uiState by viewModel.uiState.collectAsState()
    
    // State for form
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var addressDetail by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }

    // Load addresses when screen appears
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.loadUserAddresses(userId)
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor.copy(alpha = 0.95f),
                shadowElevation = 0.dp
            ) {
                Column {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(40.dp)
                                .background(Color.Black.copy(alpha = 0.05f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Gray
                            )
                        }
                        
                        Text(
                            text = "Địa chỉ giao hàng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        
                        IconButton(
                            onClick = { /* TODO: Focus add form */ },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(40.dp)
                                .background(Color.Black.copy(alpha = 0.05f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.size(24.dp),
                                tint = primaryColor
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFE5E7EB))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Section: Saved Addresses
            item {
                Text(
                    text = "Địa chỉ đã lưu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryColor)
                    }
                } else if (uiState.addresses.isEmpty()) {
                    Text(
                        text = "Chưa có địa chỉ lưu nào. Vui lòng thêm địa chỉ mới.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        uiState.addresses.forEach { address ->
                            SavedAddressCard(
                                name = address.receiverName,
                                phone = address.phone,
                                address = "${address.detailAddress}, ${address.ward}, ${address.district}, ${address.city}",
                                isDefault = address.isDefault,
                                isSelected = uiState.selectedAddressId == address.id,
                                primaryColor = primaryColor,
                                surfaceColor = surfaceColor,
                                onSelect = { viewModel.selectAddress(address.id) },
                                onSetDefault = { viewModel.setDefaultAddress(address.id) }
                            )
                        }
                    }
                }
            }
            
            item {
                HorizontalDivider(color = Color(0xFFE5E7EB))
            }

            // Section: Add New Address
            item {
                Text(
                    text = "Thêm địa chỉ mới",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Name & Phone Row
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            LabelText("HỌ VÀ TÊN")
                            CustomTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                placeholder = "Nhập họ tên"
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            LabelText("SỐ ĐIỆN THOẠI")
                            CustomTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                placeholder = "(+84) ..."
                            )
                        }
                    }
                    
                    // Location Selectors
                    Column {
                        LabelText("KHU VỰC")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FakeDropdown(text = "TP. Hồ Chí Minh", modifier = Modifier.weight(1f))
                            FakeDropdown(text = "Quận 1", modifier = Modifier.weight(1f))
                            FakeDropdown(text = "Phường Bến Nghé", modifier = Modifier.weight(1f))
                        }
                    }
                    
                    // Specific Address
                    Column {
                        LabelText("ĐỊA CHỈ CỤ THỂ")
                         CustomTextField(
                            value = addressDetail,
                            onValueChange = { addressDetail = it },
                            placeholder = "Số nhà, tên đường, tòa nhà...",
                            singleLine = false,
                            minLines = 2
                        )
                    }
                    
                    // Default Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Đặt làm địa chỉ mặc định",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = primaryColor,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }
                    
                    // Submit Button
                    Button(
                        onClick = { /* TODO: Save Address */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp), spotColor = primaryColor.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Lưu địa chỉ mới", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun SavedAddressCard(
    name: String,
    phone: String,
    address: String,
    isDefault: Boolean,
    isSelected: Boolean,
    primaryColor: Color,
    surfaceColor: Color,
    onSelect: () -> Unit = {},
    onSetDefault: () -> Unit = {}
) {
    val borderColor = if (isSelected) primaryColor else Color(0xFFE5E7EB)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(surfaceColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) primaryColor else Color.LightGray,
                modifier = Modifier.padding(top = 2.dp).clickable { onSelect() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(" | ", color = Color.Gray, fontSize = 14.sp)
                    Text(phone, color = Color.Gray, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(address, color = Color(0xFF4B5563), fontSize = 14.sp, lineHeight = 20.sp)
                
                if (isDefault) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = primaryColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "Mặc định",
                            color = primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                        )
                    }
                } else if (isSelected) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { onSetDefault() },
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = "Đặt mặc định",
                            color = primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            IconButton(onClick = { /* Edit */ }, modifier = Modifier.size(24.dp).padding(top = 0.dp)) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.LightGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun LabelText(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF6B7280),
        modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF9CA3AF)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD1D5DB),
            focusedBorderColor = Color(0xFFec3713),
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = singleLine,
        minLines = minLines
    )
}

@Composable
fun FakeDropdown(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = Color.Black,
                maxLines = 1
            )
            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}