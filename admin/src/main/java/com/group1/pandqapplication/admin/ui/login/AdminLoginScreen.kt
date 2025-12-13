package com.group1.pandqapplication.admin.ui.login

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    // Colors from HTML/Tailwind config
    val primaryColor = Color(0xFF137fec)
    val backgroundLight = Color(0xFFf6f7f8)
    val backgroundDark = Color(0xFF101922)
    val isDarkTheme = false // TODO: Get from theme state
    
    val backgroundColor = if (isDarkTheme) backgroundDark else backgroundLight
    val surfaceColor = if (isDarkTheme) Color(0xFF192633) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0F172A) // Slate-900
    val textSecondaryColor = if (isDarkTheme) Color(0xFF92adc9) else Color(0xFF64748B) // Slate-500
    val borderColor = if (isDarkTheme) Color(0xFF324d67) else Color(0xFFE2E8F0) // Slate-200
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Biometric Authentication
    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }
    
    // Check biometric capability
    val biometricCapability = remember {
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricCapability.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricCapability.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricCapability.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricCapability.NOT_ENROLLED
            else -> BiometricCapability.NOT_AVAILABLE
        }
    }
    
    // Determine button text and icon based on capability
    val (biometricButtonText, biometricIcon) = remember(biometricCapability) {
        when (biometricCapability) {
            BiometricCapability.AVAILABLE -> {
                // Check if face is available (API 30+) - simplified check
                val hasFace = android.os.Build.VERSION.SDK_INT >= 29 && 
                    context.packageManager.hasSystemFeature("android.hardware.biometrics.face")
                if (hasFace) {
                    "Xác thực bằng Face ID" to Icons.Default.Face
                } else {
                    "Xác thực bằng Vân tay" to Icons.Default.Fingerprint
                }
            }
            BiometricCapability.NOT_ENROLLED -> "Thiết lập sinh trắc học" to Icons.Default.Security
            BiometricCapability.NOT_AVAILABLE -> "Sinh trắc học không khả dụng" to Icons.Default.Security
        }
    }
    
    // Biometric prompt launcher
    fun showBiometricPrompt() {
        val activity = context as? FragmentActivity ?: return
        val executor = ContextCompat.getMainExecutor(context)
        
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onLoginSuccess()
            }
            
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Lỗi: $errString", Toast.LENGTH_SHORT).show()
            }
            
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Xác thực thất bại", Toast.LENGTH_SHORT).show()
            }
        }
        
        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Xác thực Admin")
            .setSubtitle("Sử dụng sinh trắc học để đăng nhập")
            .setNegativeButtonText("Hủy")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }

    // Shimmer effect
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerTranslate by shimmerTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )
    
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            Color.Transparent,
            Color.White.copy(alpha = 0.5f),
            Color.Transparent
        ),
        start = Offset(shimmerTranslate, 0f),
        end = Offset(shimmerTranslate + 100f, 100f), // Angeled
        tileMode = TileMode.Clamp
    )

    Scaffold(
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
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
                        .clip(CircleShape)
                        .background(if (isDarkTheme) Color(0xFF192633) else Color(0xFFE2E8F0))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (isDarkTheme) Color(0xFF192633) else Color(0xFFE2E8F0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkTheme) Color(0xFF324d67) else Color(0xFFCBD5E1))
                ) {
                    Text(
                        text = "SYSTEM ACCESS",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = textSecondaryColor,
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.size(40.dp)) // Optical balance
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Branding
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = primaryColor.copy(alpha = 0.5f))
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(primaryColor, Color(0xFF1a8cff), Color(0xFF005bb5)),
                                start = Offset(0f, 0f),
                                end = Offset(100f, 100f)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Icon",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Admin Access",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Authorized Personnel Only. Please enter your credentials to manage the store.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(280.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Form
                // Email
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Admin ID / Email",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("admin@electronics-store.com", color = textSecondaryColor.copy(alpha = 0.5f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Mail, contentDescription = null, tint = textSecondaryColor)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = borderColor,
                            cursorColor = primaryColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Password
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = textSecondaryColor.copy(alpha = 0.5f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = textSecondaryColor)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility",
                                    tint = textSecondaryColor
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = surfaceColor,
                            unfocusedContainerColor = surfaceColor,
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = borderColor,
                            cursorColor = primaryColor,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    
                    TextButton(
                        onClick = { /* TODO: Forgot Password */ },
                        modifier = Modifier.align(Alignment.End),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = primaryColor,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                
                // Buttons
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(10.dp, RoundedCornerShape(12.dp), spotColor = primaryColor.copy(alpha = 0.25f))
                        .clip(RoundedCornerShape(12.dp))
                        .background(primaryColor)
                        .clickable(onClick = onLoginSuccess), // Mock login
                    contentAlignment = Alignment.Center
                ) {
                    // Shimmer
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(shimmerBrush)
                    )
                    
                    Text(
                        text = "Log In to Dashboard",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
                    Text(
                        text = "QUICK ACCESS",
                        style = MaterialTheme.typography.labelSmall,
                        color = textSecondaryColor,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        fontWeight = FontWeight.Medium
                    )
                    androidx.compose.material3.HorizontalDivider(modifier = Modifier.weight(1f), color = borderColor)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedButton(
                    onClick = { 
                        if (biometricCapability == BiometricCapability.AVAILABLE) {
                            showBiometricPrompt()
                        } else {
                            Toast.makeText(context, "Sinh trắc học không khả dụng trên thiết bị này", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = surfaceColor,
                        contentColor = textColor
                    ),
                    enabled = biometricCapability == BiometricCapability.AVAILABLE
                ) {
                    Icon(biometricIcon, contentDescription = null, tint = primaryColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = biometricButtonText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "v2.4.0 Admin Build • Secure Connection",
                    style = MaterialTheme.typography.labelSmall,
                    color = textSecondaryColor,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

// Enum for biometric capability
enum class BiometricCapability {
    AVAILABLE,
    NOT_ENROLLED,
    NOT_AVAILABLE
}
