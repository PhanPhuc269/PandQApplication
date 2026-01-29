package com.group1.pandqapplication.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloseAccountDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (reason: String?) -> Unit
) {
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var isConfirmed by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    
    val reasons = listOf(
        "Tôi không còn sử dụng ứng dụng",
        "Tôi có tài khoản khác",
        "Lo ngại về quyền riêng tư",
        "Dịch vụ không đáp ứng nhu cầu",
        "Lý do khác"
    )

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 40.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Đóng tài khoản",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Warning message
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Khi đóng tài khoản, bạn sẽ:\n• Mất quyền truy cập tài khoản\n• Không thể đăng nhập lại\n• Mất toàn bộ voucher và điểm thưởng",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp,
                        color = Color(0xFF991B1B)
                    )
                }
                
                // Reason dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedReason ?: "Chọn lý do (tùy chọn)",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD1D5DB),
                            unfocusedBorderColor = Color(0xFFD1D5DB)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        reasons.forEach { reason ->
                            DropdownMenuItem(
                                text = { Text(reason) },
                                onClick = {
                                    selectedReason = reason
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                // Confirmation checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isConfirmed,
                        onCheckedChange = { isConfirmed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFDC2626)
                        )
                    )
                    Text(
                        text = "Tôi hiểu rằng hành động này không thể hoàn tác",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedReason) },
                enabled = isConfirmed && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626),
                    disabledContainerColor = Color(0xFFD1D5DB)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Đóng tài khoản", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Hủy", color = Color(0xFF6B7280))
            }
        }
    )
}
