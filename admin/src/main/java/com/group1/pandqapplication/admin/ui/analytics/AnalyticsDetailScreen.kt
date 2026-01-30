package com.group1.pandqapplication.admin.ui.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group1.pandqapplication.admin.ui.analytics.TopProductItem
import com.group1.pandqapplication.admin.ui.analytics.CategoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDetailScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val title = when (uiState.reportType) {
        "Doanh thu theo sản phẩm" -> "Chi tiết doanh thu sản phẩm"
        "Doanh thu theo danh mục" -> "Chi tiết doanh thu danh mục"
        "Số lượng bán theo sản phẩm" -> "Chi tiết số lượng bán sản phẩm"
        "Số lượng bán theo danh mục" -> "Chi tiết số lượng bán danh mục"
        else -> "Chi tiết báo cáo"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(uiState.error ?: "Unknown error", color = Color.Red)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.reportType.contains("danh mục")) {
                    items(uiState.categories) { category ->
                        CategoryItem(category, uiState.reportType)
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                    }
                } else {
                    items(uiState.products) { product ->
                        TopProductItem(product, uiState.reportType)
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
                    }
                }
            }
        }
    }
}
