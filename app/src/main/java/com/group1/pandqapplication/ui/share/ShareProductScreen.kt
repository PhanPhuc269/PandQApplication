package com.group1.pandqapplication.ui.share

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.CardDark
import com.group1.pandqapplication.shared.ui.theme.CardLight
import com.group1.pandqapplication.shared.ui.theme.CheckoutPrimary
import com.group1.pandqapplication.shared.ui.theme.ShareBackgroundIconDark
import com.group1.pandqapplication.shared.ui.theme.ShareBackgroundIconLight
import com.group1.pandqapplication.shared.ui.theme.ShareBorderDark
import com.group1.pandqapplication.shared.ui.theme.ShareBorderLight
import com.group1.pandqapplication.shared.ui.theme.ShareSubtleDark
import com.group1.pandqapplication.shared.ui.theme.ShareSubtleLight
import com.group1.pandqapplication.shared.ui.theme.TextDarkPrimary
import com.group1.pandqapplication.shared.ui.theme.TextLightPrimary

@Composable
fun ShareProductScreen(
    onDismissRequest: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val surfaceColor = if (isDarkTheme) CardDark else CardLight
    val textPrimary = if (isDarkTheme) TextDarkPrimary else TextLightPrimary
    val textSubtle = if (isDarkTheme) ShareSubtleDark else ShareSubtleLight
    val borderColor = if (isDarkTheme) ShareBorderDark else ShareBorderLight
    val iconBgColor = if (isDarkTheme) ShareBackgroundIconDark else ShareBackgroundIconLight
    val primaryColor = CheckoutPrimary // Reusing orange primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = if (isDarkTheme) 0.7f else 0.5f))
            .clickable(onClick = onDismissRequest), // Dismiss when clicking outside
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(surfaceColor, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .clickable(enabled = false) {} // Consume clicks inside
        ) {
            // Drag Handle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .background(borderColor, CircleShape)
                )
            }

            Text(
                text = "Share Product",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                textAlign = TextAlign.Center
            )

            // Product Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC97gD6Y1CzOpDSTC3gK3cECh4Fn6mFGzB20TP2zpRRNIIAIETcZ90HsTctCo3Atkm0lcoAysb55vHOLE7waf9I9pJDPpxf6Lmo1VCt2tCIc3LvjWgY28jniaZLIC5P2G27ypJodcOKWrJlwo1NWMFyBVbVlruHK-tNauHz1h9Cx_kdDhHUw4Ce-XMQ-lR0ty4UI9hU11F_O3hAoYjKAGahWEXF-8BFzWVUlVO1Nii4lFr0bdOddm9OwyLUHtdZ6AhNlDV1LZziylI",
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = "Galaxy S24 Ultra",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary
                    )
                    Text(
                        text = "Titanium Gray, 512GB",
                        fontSize = 14.sp,
                        color = textSubtle
                    )
                }
            }

            HorizontalDivider(color = borderColor, modifier = Modifier.padding(horizontal = 16.dp))

            // Shareable Link
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Shareable Link",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    TextField(
                        value = "https://yourapp.com/product/12345",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Transparent),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent, // Transparent because we deal with borders manually or let default handle it?
                            // Actually design has a container with border. Let's try standard approach.
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
                    // Custom implementing border and background manually for exact look is hard with just Modifiers on Row.
                    // Let's use a Box for the border container.
                }
                
                // Redoing Share Link UI for better structure
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.Transparent) // Container background
                ) {
                   TextField(
                       value = "https://yourapp.com/product/12345",
                       onValueChange = {},
                       readOnly = true,
                       modifier = Modifier
                           .weight(1f)
                           .fillMaxSize()
                           .border(
                               width = 1.dp, 
                               color = borderColor, 
                               shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                           )
                           .background(
                               color = if (isDarkTheme) Color(0xFF221310) else Color(0xFFF8F6F6),
                               shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                           ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = textPrimary,
                            unfocusedTextColor = textPrimary
                        )
                   )
                   
                   Box(
                       modifier = Modifier
                           .width(50.dp)
                           .fillMaxSize()
                           .border(
                               width = 1.dp,
                               color = borderColor, // Border
                               shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                           )
                           // Remove left border effectively by drawing over or positioning?
                           // Simple way: negative offset overlap or just standard usage.
                           // Actually the design shows connected input group.
                           .background(
                               color = if (isDarkTheme) Color(0xFF221310) else Color(0xFFF8F6F6),
                               shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                           )
                           .clickable { /* Copy */ },
                       contentAlignment = Alignment.Center
                   ) {
                       Icon(
                           imageVector = Icons.Default.ContentCopy,
                           contentDescription = "Copy",
                           tint = primaryColor,
                           modifier = Modifier.size(20.dp)
                       )
                   }
                }
            }

            // Share Via
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 32.dp)) {
                Text(
                    text = "Share via",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val apps = listOf(
                    SocialApp("Zalo", "https://lh3.googleusercontent.com/aida-public/AB6AXuCXvWDomi9vXeLMEjS0oy_9JmDB9WdbyIQBoAN-wzE2UEuETbGtrx98zgCHVE6ZXSeLuwQJcOKHGgY4Sk7-5KmmUB7VIO02VixbOoiKqu0h9QDOeNotErEcYChpfyO6bO65n8ODDJ7BhIf_cik1SfBJDlY66b_gdJ5Mimm0BelJtQFKEKm6enauezHQBcFwoCNZPZmvsSyo8E4mN9TWS7CgOpFxmoLOkfu3-C2jrfkhrc7Di_V3favv_RMPdhLM1IKOvZAyuLxkDMo"),
                    SocialApp("Messenger", "https://lh3.googleusercontent.com/aida-public/AB6AXuCS_LcT9Gol7n__79rT7e4UKvl-0UMpM6SppWzFTKpcfUaY3ewVRRpn3E-sMbWopooPXV6mnZyZ45aab1E2Iz0xTcTj2FMn-UB93o6lTVTt8_BuzqI6znZsc6foF0NuFW0DhvG2j2pm98O7f3Frl8chU7yMgGdbpk6Y9BIZyNFv3BFjEpksS21Nhic1AZO6-7k9r17_GIRDEA-5ECWF2Ay2tr6s4SLHEBjIQZ7T4OkUUpVj4wbrkJ8Tzjq_NM-B2L3s4lxl3iF7hpc"),
                    SocialApp("Instagram", "https://lh3.googleusercontent.com/aida-public/AB6AXuDt0Hj0V62p6o98mpw88EYbrBeNb7zAcB1g-TmXopYZbwR0-oWFWlYs8fJPm979k7V3AjHiDInl6MCErd0Fpyt_87K7oPfjElZj6esVh-tuiK7tUnjZo6B25NLtzEIhHAT22JCITooe4vbCah2h94nnLxcCpg9_w8OKVy7JwSW4tTC6lllXTaO1jtkBkIhCbzQ3Gs2-3XAt2jyXeoSEiwWh9XO9ty0CBCWqVJ09YBm79ypsZXf4mB1IIISCyI_tsQMBoTrcsHptaTc"),
                    SocialApp("Messages", "https://lh3.googleusercontent.com/aida-public/AB6AXuCTzHUQ_nVpUGUtz4iu1s6BPxlpubcFqbVV8cNSIwcXB3iWqXLzm7xO4ApI8uHirY_DbuL-qiikcty74_HRKH5lhluohAaJTwMIdA9a6In1N6y9oDRViz_FQ-2AceOru9zNg0MlzQZB_UsBEvj9sMArj6QjGTAy0sMfIVAeE5F07ytqDt1o1HFF_F3-xVogvqVqHi8dqd6iRQMRF__DPOYi4h8cER_eeJn2zA8aMtUCyhRpaQU-5O2Itbjb9d1euK2XytvhAMup3LI", isDarkSupport = true),
                    SocialApp("Facebook", "https://lh3.googleusercontent.com/aida-public/AB6AXuAO3mZInYFdMjb0JY2ROd3Uz5_JlrGYw9WbBGAB2WUtrjN-qGa9tR7_f2pAPAH5yic3bfFCA2nGZQFY0nkrSGmy1KP4Giq4z-0KnPy-ySkgpKJXOLl3I2rxw-jm0OUOTHgB6LdW_-PClJ3xN3FI0VLMb3UdNXUvST7z0kQKJOPMAfJMzDU2WJC2-Y1fhdpWIj35SM9h4gMFdq0FUeceFgaz9A9g3QQCygspukvNA_WyGqXaA1_ajuUiDmoEuf8wkC3N_Kl-aJGEaoQ"),
                    SocialApp("More", "") // Special case
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(apps) { app ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(iconBgColor, RoundedCornerShape(12.dp))
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                if (app.name == "More") {
                                    Icon(
                                        imageVector = Icons.Default.MoreHoriz,
                                        contentDescription = "More",
                                        tint = textPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                } else {
                                    AsyncImage(
                                        model = app.iconUrl,
                                        contentDescription = app.name,
                                        modifier = Modifier.size(32.dp),
                                        colorFilter = if (isDarkTheme && app.isDarkSupport) ColorFilter.tint(Color.White) else null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = app.name,
                                fontSize = 12.sp,
                                color = textSubtle,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

data class SocialApp(
    val name: String,
    val iconUrl: String,
    val isDarkSupport: Boolean = false
)

@Preview
@Composable
fun PreviewShareProductScreen() {
    ShareProductScreen()
}
