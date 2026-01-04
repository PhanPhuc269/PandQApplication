package com.group1.pandqapplication.ui.address

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAddressScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMap: () -> Unit = {},
    onMapResultReceived: ((String) -> Unit)? = null,
    viewModel: AddEditAddressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
            onNavigateBack()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    // Check for map selection result when returning from MapPicker
    LaunchedEffect(Unit) {
        MapSelectionHolder.getAndClearAddress()?.let { address ->
            viewModel.fillFromMapAddress(address)
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 2.dp,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        if (viewModel.isEditMode) "Sửa địa chỉ" else "Thêm địa chỉ mới",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF111827)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Receiver Name
                AddressFormField(
                    label = "Tên người",
                    value = uiState.receiverName,
                    onValueChange = { viewModel.updateReceiverName(it) },
                    icon = Icons.Filled.Person,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Map Picker Button (Shopee-style)
                OutlinedButton(
                    onClick = onNavigateToMap,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = primaryColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor)
                ) {
                    Icon(
                        Icons.Filled.Map,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Chọn vị trí trên bản đồ",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phone
                AddressFormField(
                    label = "Số điện thoại",
                    value = uiState.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    icon = Icons.Filled.Phone,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Address
                AddressFormField(
                    label = "Địa chỉ chi tiết",
                    value = uiState.detailAddress,
                    onValueChange = { viewModel.updateDetailAddress(it) },
                    icon = Icons.Filled.Home,
                    primaryColor = primaryColor,
                    placeholder = "Số nhà, tên đường..."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Ward
                AddressFormField(
                    label = "Phường/Xã",
                    value = uiState.ward,
                    onValueChange = { viewModel.updateWard(it) },
                    icon = Icons.Filled.LocationOn,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // District
                AddressFormField(
                    label = "Quận/Huyện",
                    value = uiState.district,
                    onValueChange = { viewModel.updateDistrict(it) },
                    icon = Icons.Filled.LocationCity,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // City
                AddressFormField(
                    label = "Tỉnh/Thành phố",
                    value = uiState.city,
                    onValueChange = { viewModel.updateCity(it) },
                    icon = Icons.Filled.Map,
                    primaryColor = primaryColor
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Default checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.isDefault,
                        onCheckedChange = { viewModel.toggleDefault() },
                        colors = CheckboxDefaults.colors(checkedColor = primaryColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Đặt làm địa chỉ mặc định",
                        fontSize = 14.sp,
                        color = Color(0xFF374151)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = { viewModel.saveAddress(onNavigateBack) },
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        disabledContainerColor = primaryColor.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Lưu địa chỉ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun AddressFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    primaryColor: Color,
    placeholder: String = ""
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(placeholder, color = Color(0xFF9CA3AF))
                }
            },
            leadingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = Color(0xFFD1D5DB),
                focusedLeadingIconColor = primaryColor,
                unfocusedLeadingIconColor = Color(0xFF9CA3AF)
            ),
            singleLine = true
        )
    }
}
