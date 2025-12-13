package com.group1.pandqapplication.admin.ui.setting

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Update
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.SettingsIconBgDark
import com.group1.pandqapplication.shared.ui.theme.SettingsIconBgLight
import com.group1.pandqapplication.shared.ui.theme.SettingsSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.SettingsSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.SettingsTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.SettingsTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.SettingsTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.SettingsTextSecondaryLight

@Composable
fun AdminSettingsScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val surfaceColor = if (isDarkTheme) SettingsSurfaceDark else SettingsSurfaceLight
    val textPrimary = if (isDarkTheme) SettingsTextPrimaryDark else SettingsTextPrimaryLight
    val textSecondary = if (isDarkTheme) SettingsTextSecondaryDark else SettingsTextSecondaryLight
    val iconBgColor = if (isDarkTheme) SettingsIconBgDark else SettingsIconBgLight

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint = textPrimary
                    )
                }
                Text(
                    text = "Cài đặt",
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
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida-public/AB6AXuDhseOwGn5wt-Qonlzclmbg28_VixzFNtwWAIhSZIxrQ73UUZrBDLnVGbUuNFYaG5UW1T97UL3IpUWvCi4-NMC-erB4TxllxJAOIJB_cj2IRUwiFuz-bHePG_J0IzdV8muPa2vo8FFER6x7DXodYdAXHR0DqidNzUhEmlLxG9ALJE7V7KoKmW3kQIfvQ0Z5rcsTOte7RAXf_9vjgUAeuOAkaPh3ekjCBgFXPACv9nsWq9upP2YcAMe24g59p0rqvt4Yys_1KdHhEs0",
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Nguyen Van A",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                "Administrator",
                                fontSize = 16.sp,
                                color = textSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBgColor) // Using icon bg for button bg as per HTML (stone-200/800)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                     Text("Edit Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }
            }

            // Account Security
            SettingsGroup("Account Security", surfaceColor, textPrimary) {
                SettingsItem(Icons.Default.Lock, "Change Password", textPrimary, textSecondary, iconBgColor)
                SettingsItem(Icons.Default.Shield, "Two-Factor Authentication", textPrimary, textSecondary, iconBgColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Notification Settings
            SettingsGroup("Notification Settings", surfaceColor, textPrimary) {
                var newOrders by remember { mutableStateOf(true) }
                var lowStock by remember { mutableStateOf(true) }
                var sysUpdates by remember { mutableStateOf(false) }

                ToggleItem(Icons.Default.Notifications, "New Order Notifications", newOrders, { newOrders = it }, textPrimary, iconBgColor)
                ToggleItem(Icons.Default.Inventory2, "Low Stock Alerts", lowStock, { lowStock = it }, textPrimary, iconBgColor)
                ToggleItem(Icons.Default.Update, "System Updates", sysUpdates, { sysUpdates = it }, textPrimary, iconBgColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Application
            SettingsGroup("Application", surfaceColor, textPrimary) {
                InfoItem(Icons.Default.Contrast, "Appearance", "Dark", textPrimary, textSecondary, iconBgColor)
                InfoItem(Icons.Default.Language, "Language", "English", textPrimary, textSecondary, iconBgColor)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Logout
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(surfaceColor)
                    .clickable { }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                 Text("Log Out", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Red)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    surfaceColor: Color,
    titleColor: Color,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(surfaceColor)
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    textPrimary: Color,
    textSecondary: Color,
    iconBgColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp), // min-h-[56px] ~ 14dp padding vertical effectively
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = textPrimary, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = textPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun ToggleItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textPrimary: Color,
    iconBgColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = textPrimary, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = textPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ProductPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = iconBgColor
            )
        )
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    title: String,
    value: String,
    textPrimary: Color,
    textSecondary: Color,
    iconBgColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = textPrimary, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = textPrimary, modifier = Modifier.weight(1f))
        Text(value, fontSize = 16.sp, color = textSecondary)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = textSecondary, modifier = Modifier.size(24.dp))
    }
}

@Preview
@Composable
fun PreviewAdminSettingsScreen() {
    AdminSettingsScreen()
}
