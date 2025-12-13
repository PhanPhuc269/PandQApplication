package com.group1.pandqapplication.admin.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.group1.pandqapplication.shared.ui.theme.PandQApplicationTheme

@Composable
fun AdminProfileScreen(
    onBackClick: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    Scaffold(
        topBar = { ProfileTopAppBar(onBackClick = onBackClick, onSettingsClick = onNavigateToSettings) },
        bottomBar = {
            ProfileBottomBar()
        },
        containerColor = MaterialTheme.colorScheme.background // light: #f6f7f8, dark: #101922
    ) { paddingValues ->
        Column(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(paddingValues)
                 .verticalScroll(rememberScrollState())
                 .background(MaterialTheme.colorScheme.background)
                 .padding(bottom = 100.dp) // Space for bottom bar
        ) {
            // Profile Header
            ProfileHeader()

            // Personal Information
            PersonalInformationSection()

            Divider(modifier = Modifier.padding(16.dp), color = Color.LightGray.copy(alpha = 0.3f))

            // Security Settings
            SecuritySection()

            Divider(modifier = Modifier.padding(16.dp), color = Color.LightGray.copy(alpha = 0.3f))

            // Assigned Roles
            AssignedRolesSection()
        }
    }
}

@Composable
fun ProfileTopAppBar(onBackClick: () -> Unit, onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
        }
        Text(
            text = "Admin Profile",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBhJ9kCXDSddiDy3Y5-3VUPwI6nLZoR2yZTnGULbMO0gAGQu1F4CyqRCjM64o0FTLQ2nkYni0WCL8F9eJciIduaksWUL7DocOovEoCsVYPszL7PDONk6bQQ485jYSwUHYHijq5VTdWLosqhyzI_kXz2t3hywTtSChQ1EA7VAKeOJLnU9Nq_Ry_axjM6PhNf_x2ZlbPFI9ASPfK_B_peUaxel4sYwUHPp0mioQfa6AGmYfaQlMiBjDeYPxIHR2lRO8FPTwnmItqgvw0",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .shadow(4.dp, CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { /* Edit Avatar */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(36.dp)
                    .background(Color(0xFF137fec), CircleShape) // Primary
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text(
            text = "Alex Johnson",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Surface(
            color = Color(0xFFE0F2FE), // blue-100
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF137fec), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Super Admin",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF137fec)
                )
            }
        }
    }
}

@Composable
fun PersonalInformationSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileTextField(label = "First Name", value = "Alex", modifier = Modifier.weight(1f))
            ProfileTextField(label = "Last Name", value = "Johnson", modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Email (Read Only)
        Column {
            Text(
                text = "Email Address (Read-only)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "admin@electronics.com",
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    color = Color.Gray
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        ProfileTextField(label = "Phone Number", value = "+84 90 123 4567")
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ProfileTextField(label = "Department", value = "Inventory Management")
    }
}

@Composable
fun ProfileTextField(
    label: String, 
    value: String, 
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            readOnly = readOnly,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.5f),
                focusedIndicatorColor = Color(0xFF137fec).copy(alpha = 0.5f)
            ),
            singleLine = true
        )
    }
}

@Composable
fun SecuritySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Security",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        // Change Password
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { },
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEFF6FF), RoundedCornerShape(8.dp)), // blue-50
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF137fec))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Change Password",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Last updated 30 days ago",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Face ID
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFAF5FF), RoundedCornerShape(8.dp)), // purple-50
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Face, contentDescription = null, tint = Color(0xFF9333EA)) // purple-600
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Face ID Login",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Enable biometric access",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                Switch(
                    checked = true,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF137fec)
                    )
                )
            }
        }
    }
}

@Composable
fun AssignedRolesSection() {
    val roles = listOf(
        RoleChipData("Super Admin", Icons.Default.AdminPanelSettings, Color(0xFF1D4ED8), Color(0xFFDBEAFE)), // Blue
        RoleChipData("Edit Stock", Icons.Default.Inventory, Color(0xFF047857), Color(0xFFD1FAE5)), // Emerald
        RoleChipData("View Analytics", Icons.Default.BarChart, Color(0xFF4338CA), Color(0xFFE0E7FF)), // Indigo
        RoleChipData("Manage Orders", Icons.Default.ShoppingCart, Color(0xFF92400E), Color(0xFFFEF3C7)) // Amber
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = "Assigned Roles",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            roles.forEach { role ->
                RoleChip(role)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    // Basic FlowRow implementation or usage of accompanist/compose 1.4+ FlowRow
    // Since basic FlowRow is available in newer compose versions, trying standardized Layout
    // If not available, we map it to Row/Column logic or use a LazyGrid
    // For simplicity with standard Compose provided in environment, assume standard FlowRow exists or mock with Row/Wrap
    
    // Using a simple Row for now if items few, but user asked for wrap. 
    // Assuming androidx.compose.foundation.layout.FlowRow is available (Compose 1.4+)
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}


data class RoleChipData(val name: String, val icon: ImageVector, val textColor: Color, val bgColor: Color)

@Composable
fun RoleChip(data: RoleChipData) {
    Surface(
        color = data.bgColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, data.textColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(data.icon, contentDescription = null, tint = data.textColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = data.name,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = data.textColor
            )
        }
    }
}

@Composable
fun ProfileBottomBar() {
    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(
                onClick = { /* Save */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137fec))
            ) {
                Text(
                    text = "Save Changes",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "VER 1.0.2",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                    color = Color.Gray
                )
                
                Row(
                    modifier = Modifier.clickable { /* Logout */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Log Out",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AdminProfileScreenPreview() {
    PandQApplicationTheme {
        AdminProfileScreen()
    }
}
