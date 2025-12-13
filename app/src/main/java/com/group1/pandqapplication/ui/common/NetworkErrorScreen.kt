package com.group1.pandqapplication.ui.common

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.BackgroundLight

@Composable
fun NetworkErrorScreen(
    onRetry: () -> Unit = {}
) {
    val context = LocalContext.current
    val primaryColor = Color(0xFFec3713)
    val backgroundColor = BackgroundLight

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Icon Container with Badge
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer Glow/Blur effect simulation (simplified)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(primaryColor.copy(alpha = 0.1f))
                )
                
                // Main Icon Circle
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.White.copy(alpha = 0.8f), Color.White.copy(alpha = 0.4f))
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                        .padding(4.dp), // Simulation of ring
                    contentAlignment = Alignment.Center
                ) {
                     Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                     ) {
                         Icon(
                             Icons.Outlined.WifiOff,
                             contentDescription = null,
                             modifier = Modifier.size(64.dp),
                             tint = Color.Gray // text-neutral-400
                         )
                     }
                }

                // Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-8).dp, y = (8).dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(primaryColor)
                        .border(4.dp, backgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.PriorityHigh,
                        contentDescription = "Alert",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Text Content
            Text(
                "Mất kết nối mạng",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF171717), // neutral-900
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                "Có vẻ như bạn đang ngoại tuyến. Vui lòng kiểm tra đường truyền Wi-Fi hoặc dữ liệu di động và thử lại.",
                fontSize = 16.sp,
                color = Color(0xFF525252), // neutral-600
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Column(
                modifier = Modifier.widthIn(max = 320.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Retry Button
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thử lại", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Settings Button
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF525252)
                    ),
                    border = null, // "border border-transparent" in HTML but visual result usually implies minimal or no border for ghost button
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kiểm tra cài đặt", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
fun NetworkErrorScreenPreview() {
    NetworkErrorScreen()
}
