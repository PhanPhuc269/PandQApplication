package com.group1.pandqapplication.ui.account

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import com.group1.pandqapplication.R
import com.group1.pandqapplication.ui.components.LanguagePickerDialog
import com.group1.pandqapplication.util.LocaleManager
import kotlinx.coroutines.launch
import com.group1.pandqapplication.ui.notification.NotificationSettingsDialog
import com.group1.pandqapplication.shared.data.remote.dto.NotificationPreferenceRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToPersonalInfo: () -> Unit = {},
    onNavigateToAddressList: () -> Unit = {},
    onNavigateToSupport: () -> Unit = {},
    onNavigateToUserGuide: () -> Unit = {},
    onNavigateToPolicy: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundColor = Color(0xFFF8F6F6)
    val primaryColor = Color(0xFFec3713)
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showLanguageDialog by remember { mutableStateOf(false) }
    var currentLanguage by remember { mutableStateOf(LocaleManager.getCurrentLanguage(context)) }
    var showNotificationSettings by remember { mutableStateOf(false) }
    var showCloseAccountDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                color = backgroundColor,
                shadowElevation = 1.dp
            ) {
                Column {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                         IconButton(onClick = {}) {
                             Icon(Icons.Filled.ArrowBackIosNew, contentDescription = stringResource(R.string.back), tint = Color(0xFF1F2937))
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                             Text(
                                 stringResource(R.string.personal_profile), 
                                 fontWeight = FontWeight.Bold, 
                                 fontSize = 18.sp, 
                                 color = Color(0xFF111827)
                             ) 
                        }
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
        ) {
            // Profile Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar - show user photo or default icon
                if (uiState.photoUrl != null) {
                    AsyncImage(
                        model = uiState.photoUrl,
                        contentDescription = stringResource(R.string.avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                    )
                } else {
                    // Default avatar with icon
                    Box(
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE5E7EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = stringResource(R.string.default_avatar),
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFF9CA3AF)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    uiState.displayName, 
                    fontSize = 22.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFF111827)
                )
                Text(
                    uiState.email, 
                    fontSize = 16.sp, 
                    color = Color(0xFF6B7280)
                )
            }
            
            // Email Verification Banner
            if (uiState.isLoggedIn && !uiState.isEmailVerified) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = Color(0xFFFEF3C7), // amber-100
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = null,
                                tint = Color(0xFFD97706), // amber-600
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                stringResource(R.string.email_not_verified),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E) // amber-800
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(R.string.email_verify_prompt),
                            fontSize = 14.sp,
                            color = Color(0xFFB45309) // amber-700
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.sendVerificationEmail() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFD97706))
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = Color(0xFFD97706),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(stringResource(R.string.resend_email), color = Color(0xFFD97706), fontSize = 13.sp)
                                }
                            }
                            OutlinedButton(
                                onClick = { viewModel.refreshEmailVerificationStatus() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFD97706))
                            ) {
                                Text(stringResource(R.string.already_verified), color = Color(0xFFD97706), fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader(title = stringResource(R.string.section_account))
            SectionContainer {
                SectionItem(
                    icon = Icons.Outlined.Person, 
                    label = stringResource(R.string.personal_info), 
                    primaryColor = primaryColor,
                    onClick = onNavigateToPersonalInfo
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Outlined.Home, 
                    label = stringResource(R.string.address_book), 
                    primaryColor = primaryColor,
                    onClick = onNavigateToAddressList
                )
            }
            
            // Settings Section
            SectionHeader(title = stringResource(R.string.section_settings))
            SectionContainer {
                SectionItem(
                    icon = Icons.Outlined.Notifications, 
                    label = stringResource(R.string.notification_settings), 
                    primaryColor = primaryColor,
                    onClick = { showNotificationSettings = true }
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Outlined.Settings, 
                    label = stringResource(R.string.app_settings), 
                    primaryColor = primaryColor
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                // Language Settings
                SectionItemWithSubtitle(
                    icon = Icons.Outlined.Language,
                    label = stringResource(R.string.language_settings),
                    subtitle = LocaleManager.getLanguageDisplayName(currentLanguage),
                    primaryColor = primaryColor,
                    onClick = { showLanguageDialog = true }
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Outlined.DeleteForever, 
                    label = stringResource(R.string.close_account), 
                    isDestructive = true,
                    primaryColor = primaryColor,
                    onClick = { showCloseAccountDialog = true }
                )
            }
            
            // Support Section
            Spacer(modifier = Modifier.height(20.dp))
            SectionContainer {
                SectionItem(
                    icon = Icons.Outlined.HelpOutline, 
                    label = stringResource(R.string.support), 
                    primaryColor = primaryColor,
                    onClick = onNavigateToSupport
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Outlined.Info, 
                    label = stringResource(R.string.user_guide), 
                    primaryColor = primaryColor,
                    onClick = onNavigateToUserGuide
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Outlined.Description, 
                    label = stringResource(R.string.policy_terms), 
                    primaryColor = primaryColor,
                    onClick = onNavigateToPolicy
                )
                HorizontalDivider(color = Color(0xFFE5E7EB), modifier = Modifier.padding(horizontal = 16.dp))
                SectionItem(
                    icon = Icons.Filled.Logout, 
                    label = stringResource(R.string.logout), 
                    isDestructive = true, 
                    primaryColor = primaryColor,
                    onClick = onLogout
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
    
    // Language Picker Dialog
    if (showLanguageDialog) {
        LanguagePickerDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { newLanguage ->
                coroutineScope.launch {
                    LocaleManager.setLanguage(context, newLanguage)
                    currentLanguage = newLanguage
                    showLanguageDialog = false
                    // Recreate activity to apply new locale
                    (context as? Activity)?.recreate()
                }
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showNotificationSettings) {
        NotificationSettingsDialog(
            preferences = uiState.preferences,
            onDismiss = { showNotificationSettings = false },
            onUpdate = { request -> viewModel.updatePreference(request) }
        )
    }

    if (showCloseAccountDialog) {
        CloseAccountDialog(
            isLoading = uiState.isClosingAccount,
            onDismiss = { showCloseAccountDialog = false },
            onConfirm = { reason -> viewModel.closeAccount(reason) }
        )
    }

    // Handle close account success
    LaunchedEffect(uiState.closeAccountSuccess) {
        if (uiState.closeAccountSuccess) {
            showCloseAccountDialog = false
            onLogout() // Logout user after successful closure
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SectionContainer(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        color = Color.White.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun SectionItem(
    icon: ImageVector, 
    label: String, 
    primaryColor: Color, 
    isDestructive: Boolean = false,
    onClick: () -> Unit = {}
) {
    val contentColor = if (isDestructive) Color.Red else Color(0xFF1F2937)
    val iconBgColor = if (isDestructive) Color.Red.copy(alpha = 0.1f) else primaryColor.copy(alpha = 0.1f)
    val iconTint = if (isDestructive) Color.Red else primaryColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
         Box(
             modifier = Modifier
                 .size(40.dp)
                 .clip(RoundedCornerShape(8.dp))
                 .background(iconBgColor),
             contentAlignment = Alignment.Center
         ) {
             Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
         }
         Text(
             text = label,
             modifier = Modifier
                 .weight(1f)
                 .padding(start = 16.dp),
             fontSize = 16.sp,
             color = contentColor
         )
         if (!isDestructive) {
             Icon(
                 Icons.Filled.ChevronRight, 
                 contentDescription = null, 
                 tint = Color(0xFF9CA3AF)
             )
         }
    }
}

@Composable
fun SectionItemWithSubtitle(
    icon: ImageVector, 
    label: String,
    subtitle: String,
    primaryColor: Color,
    onClick: () -> Unit = {}
) {
    val iconBgColor = primaryColor.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
         Box(
             modifier = Modifier
                 .size(40.dp)
                 .clip(RoundedCornerShape(8.dp))
                 .background(iconBgColor),
             contentAlignment = Alignment.Center
         ) {
             Icon(imageVector = icon, contentDescription = null, tint = primaryColor, modifier = Modifier.size(24.dp))
         }
         Column(
             modifier = Modifier
                 .weight(1f)
                 .padding(start = 16.dp)
         ) {
             Text(
                 text = label,
                 fontSize = 16.sp,
                 color = Color(0xFF1F2937)
             )
             Text(
                 text = subtitle,
                 fontSize = 13.sp,
                 color = Color(0xFF6B7280)
             )
         }
         Icon(
             Icons.Filled.ChevronRight, 
             contentDescription = null, 
             tint = Color(0xFF9CA3AF)
         )
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AccountScreen(onLogout = {})
}
