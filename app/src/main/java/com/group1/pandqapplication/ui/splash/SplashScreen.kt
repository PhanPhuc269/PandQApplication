package com.group1.pandqapplication.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.group1.pandqapplication.R
import com.group1.pandqapplication.ui.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController,
    onInitializationComplete: (Boolean) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is SplashState.Success) {
            onInitializationComplete((uiState as SplashState.Success).isFirstLaunch)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is SplashState.Loading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // App logo
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = "PandQ Logo",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }
            is SplashState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Lỗi khởi tạo", style = MaterialTheme.typography.titleLarge, color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(state.message, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Thử lại")
                    }
                }
            }
            else -> {} // Success handled by LaunchedEffect
        }
    }
}
