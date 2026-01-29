package com.group1.pandqapplication.ui.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceResponse

@Composable
fun NotificationSettingsDialog(
    preferences: NotificationPreferenceResponse?,
    onDismiss: () -> Unit,
    onUpdate: (NotificationPreferenceRequest) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cài đặt thông báo", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                if (preferences == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    NotificationSwitchRow("Khuyến mãi", preferences.enablePromotions) {
                        onUpdate(NotificationPreferenceRequest(enablePromotions = it))
                    }
                    NotificationSwitchRow("Đơn hàng", preferences.enableOrders) {
                        onUpdate(NotificationPreferenceRequest(enableOrders = it))
                    }
                    NotificationSwitchRow("Tin nhắn", preferences.enableChat) {
                        onUpdate(NotificationPreferenceRequest(enableChat = it))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = Color(0xFFec3713))
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun NotificationSwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFec3713), checkedTrackColor = Color(0xFFFFDBCF))
        )
    }
}
