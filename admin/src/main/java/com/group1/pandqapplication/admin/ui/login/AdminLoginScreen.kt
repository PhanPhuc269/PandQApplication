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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
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
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit = {},
    viewModel: AdminAuthViewModel = hiltViewModel()
) {
    // Collect auth state
    val authState by viewModel.authState.collectAsState()
    val passwordResetState by viewModel.passwordResetState.collectAsState()

    // Colors
    val primaryColor = Color(0xFF137fec)
    val backgroundLight = Color(0xFFf6f7f8)
    val surfaceColor = Color.White
    val textColor = Color(0xFF0F172A)
    val textSecondaryColor = Color(0xFF64748B)
    val borderColor = Color(0xFFE2E8F0)

    // Get email from existing session if available
    val existingEmail = (authState as? AdminAuthState.Authenticated)?.email ?: ""
    var email by remember { mutableStateOf(existingEmail) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    // Update email when auth state changes to Authenticated
    LaunchedEffect(authState) {
        if (authState is AdminAuthState.Authenticated) {
            email = (authState as AdminAuthState.Authenticated).email
        }
    }

    // Biometric setup
    val context = LocalContext.current
    val biometricManager = remember { BiometricManager.from(context) }

    val biometricCapability = remember {
        when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or 
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricCapability.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricCapability.NOT_ENROLLED
            else -> BiometricCapability.NOT_AVAILABLE
        }
    }

    val (biometricButtonText, biometricIcon) = remember(biometricCapability) {
        when (biometricCapability) {
            BiometricCapability.AVAILABLE -> {
                val hasFace = android.os.Build.VERSION.SDK_INT >= 29 &&
                    context.packageManager.hasSystemFeature("android.hardware.biometrics.face")
                if (hasFace) "Xác thực bằng Face ID" to Icons.Default.Face
                else "Xác thực bằng Vân tay" to Icons.Default.Fingerprint
            }
            BiometricCapability.NOT_ENROLLED -> "Thiết lập sinh trắc học" to Icons.Default.Security
            BiometricCapability.NOT_AVAILABLE -> "Sinh trắc học không khả dụng" to Icons.Default.Security
        }
    }

    // Show biometric prompt for existing session
    fun showBiometricPrompt() {
        val activity = context as? FragmentActivity ?: return
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Verify admin role after biometric success
                viewModel.verifyAfterBiometric()
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
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or 
                BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    // Track if we should navigate (to avoid issues with recomposition)
    var shouldNavigate by remember { mutableStateOf(false) }

    // Handle navigation separately
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            android.util.Log.d("AdminLogin", "shouldNavigate is TRUE - navigating NOW")
            onLoginSuccess()
        }
    }

    // Handle auth state changes
    LaunchedEffect(authState) {
        android.util.Log.d("AdminLogin", "Auth state changed: $authState")
        when (val state = authState) {
            is AdminAuthState.Success -> {
                android.util.Log.d("AdminLogin", "SUCCESS - Setting shouldNavigate = true")
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                shouldNavigate = true
            }
            is AdminAuthState.Error -> {
                android.util.Log.d("AdminLogin", "ERROR - ${state.message}")
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
            else -> {}
        }
    }

    // Handle password reset state
    LaunchedEffect(passwordResetState) {
        when (val state = passwordResetState) {
            is PasswordResetState.Success -> {
                Toast.makeText(
                    context,
                    "Đã gửi email đặt lại mật khẩu đến ${state.email}",
                    Toast.LENGTH_LONG
                ).show()
                showForgotPasswordDialog = false
                viewModel.clearPasswordResetState()
            }
            is PasswordResetState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.clearPasswordResetState()
            }
            else -> {}
        }
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
        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.5f), Color.Transparent),
        start = Offset(shimmerTranslate, 0f),
        end = Offset(shimmerTranslate + 100f, 100f),
        tileMode = TileMode.Clamp
    )

    val isLoading = authState is AdminAuthState.Loading
    val hasExistingSession = authState is AdminAuthState.Authenticated

    Scaffold(containerColor = backgroundLight) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE2E8F0))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFE2E8F0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
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

                Spacer(modifier = Modifier.size(40.dp))
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
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = Color.White, modifier = Modifier.size(32.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (hasExistingSession) "Chào mừng trở lại" else "Admin Access",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (hasExistingSession) {
                        if (biometricCapability == BiometricCapability.AVAILABLE) {
                            "Xác thực sinh trắc học để tiếp tục"
                        } else {
                            "Nhập mật khẩu để đăng nhập lại"
                        }
                    } else {
                        "Vui lòng đăng nhập để quản lý cửa hàng"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(280.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Show password field for re-login when biometric is not available
                val showPasswordForRelogin = hasExistingSession && biometricCapability != BiometricCapability.AVAILABLE

                // Email field (show when no session OR when need password relogin)
                if (!hasExistingSession || showPasswordForRelogin) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Admin Email", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("admin@pandq.com", color = textSecondaryColor.copy(alpha = 0.5f)) },
                            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = textSecondaryColor) },
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
                            enabled = !isLoading,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Password", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("••••••••", color = textSecondaryColor.copy(alpha = 0.5f)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = textSecondaryColor) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Password",
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
                            enabled = !isLoading,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        TextButton(
                            onClick = { showForgotPasswordDialog = true },
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                        ) {
                            Text("Quên mật khẩu?", color = primaryColor, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Login Button
                if (!hasExistingSession || showPasswordForRelogin) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(10.dp, RoundedCornerShape(12.dp), spotColor = primaryColor.copy(alpha = 0.25f))
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isLoading) primaryColor.copy(alpha = 0.7f) else primaryColor)
                            .clickable(enabled = !isLoading) { viewModel.login(email, password) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isLoading) {
                            Box(modifier = Modifier.fillMaxSize().background(shimmerBrush))
                        }

                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                text = "Đăng Nhập",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
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
                }

                // Biometric Button - only show when biometric available, or for first login
                if (biometricCapability == BiometricCapability.AVAILABLE || !hasExistingSession) {
                    OutlinedButton(
                        onClick = {
                            if (biometricCapability == BiometricCapability.AVAILABLE) {
                                if (hasExistingSession) {
                                    showBiometricPrompt()
                                } else {
                                    Toast.makeText(context, "Vui lòng đăng nhập trước để sử dụng sinh trắc học", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Sinh trắc học không khả dụng", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = surfaceColor, contentColor = textColor),
                        enabled = biometricCapability == BiometricCapability.AVAILABLE && !isLoading
                    ) {
                        Icon(biometricIcon, contentDescription = null, tint = primaryColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(biometricButtonText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    }
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

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        var forgotPasswordEmail by remember { mutableStateOf(email) }
        val isResetLoading = passwordResetState is PasswordResetState.Loading

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { 
                if (!isResetLoading) showForgotPasswordDialog = false 
            },
            title = {
                Text(
                    "Quên mật khẩu",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        "Nhập email của bạn để nhận link đặt lại mật khẩu.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondaryColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = forgotPasswordEmail,
                        onValueChange = { forgotPasswordEmail = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        leadingIcon = { 
                            Icon(Icons.Default.Mail, contentDescription = null) 
                        },
                        singleLine = true,
                        enabled = !isResetLoading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
            },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = { viewModel.sendPasswordResetEmail(forgotPasswordEmail) },
                    enabled = !isResetLoading && forgotPasswordEmail.isNotBlank()
                ) {
                    if (isResetLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Gửi email")
                    }
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showForgotPasswordDialog = false },
                    enabled = !isResetLoading
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}

enum class BiometricCapability {
    AVAILABLE,
    NOT_ENROLLED,
    NOT_AVAILABLE
}
