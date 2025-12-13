package com.group1.pandqapplication.admin.ui.branch

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.BranchActive
import com.group1.pandqapplication.shared.ui.theme.BranchBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.BranchBackgroundLight
import com.group1.pandqapplication.shared.ui.theme.BranchItemSurfaceDark
import com.group1.pandqapplication.shared.ui.theme.BranchItemSurfaceLight
import com.group1.pandqapplication.shared.ui.theme.BranchPrimary
import com.group1.pandqapplication.shared.ui.theme.BranchTextMainDark
import com.group1.pandqapplication.shared.ui.theme.BranchTextMainLight
import com.group1.pandqapplication.shared.ui.theme.BranchTextSubDark
import com.group1.pandqapplication.shared.ui.theme.BranchTextSubLight

@Composable
fun BranchManagementScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) BranchBackgroundDark else BranchBackgroundLight
    val textMain = if (isDarkTheme) BranchTextMainDark else BranchTextMainLight
    val textSub = if (isDarkTheme) BranchTextSubDark else BranchTextSubLight
    val itemSurface = if (isDarkTheme) BranchItemSurfaceDark else BranchItemSurfaceLight
    val searchBg = if (isDarkTheme) Color(0xFF374151).copy(alpha = 0.4f) else Color(0xFFE5E7EB).copy(alpha = 0.6f) // Custom alpha from design

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor.copy(alpha = 0.85f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = if (isDarkTheme) Color.White else Color(0xFF1F2937)
                        )
                    }
                    Text(
                        text = "Quản lý chi nhánh",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMain,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = { /* Add Branch */ },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = BranchPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
               Row(
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(48.dp)
                       .background(searchBg, RoundedCornerShape(8.dp)),
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   Box(
                       modifier = Modifier
                           .width(48.dp)
                           .fillMaxSize(),
                       contentAlignment = Alignment.Center
                   ) {
                       Icon(
                           imageVector = Icons.Default.Search,
                           contentDescription = "Search",
                           tint = textSub
                       )
                   }
                   TextField(
                       value = "",
                       onValueChange = {},
                       placeholder = { Text("Tìm kiếm theo tên hoặc địa chỉ", color = textSub) },
                       modifier = Modifier.weight(1f),
                       colors = TextFieldDefaults.colors(
                           focusedContainerColor = Color.Transparent,
                           unfocusedContainerColor = Color.Transparent,
                           focusedIndicatorColor = Color.Transparent,
                           unfocusedIndicatorColor = Color.Transparent,
                           focusedTextColor = textMain,
                           unfocusedTextColor = textMain
                       ),
                       singleLine = true
                   )
               }
            }

            // Branch List
            val branches = listOf(
                Branch("Chi nhánh Quận 1", "123 Nguyễn Huệ, P. Bến Nghé, Quận 1, TP.HCM", true),
                Branch("Chi nhánh Tân Bình", "456 Cộng Hòa, P. 12, Quận Tân Bình, TP.HCM", true),
                Branch("Chi nhánh Thủ Đức", "789 Võ Văn Ngân, P. Linh Chiểu, TP. Thủ Đức", true),
                Branch("Chi nhánh Quận 7", "101 Tôn Dật Tiên, P. Tân Phú, Quận 7, TP.HCM", false),
                Branch("Chi nhánh Bình Thạnh", "246 Xô Viết Nghệ Tĩnh, P. 21, Q. Bình Thạnh", true)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                items(branches) { branch ->
                    BranchItem(
                        branch = branch,
                        backgroundColor = itemSurface,
                        textMain = textMain,
                        textSub = textSub
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun BranchItem(
    branch: Branch,
    backgroundColor: Color,
    textMain: Color,
    textSub: Color
) {
    val isActive = branch.isActive
    val iconColor = if (isActive) BranchPrimary else Color.Gray
    val iconBg = if (isActive) BranchPrimary.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f)
    val statusColor = if (isActive) BranchActive else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBg, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = iconColor
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = branch.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = if (isActive) textMain else textSub // Grey out name if inactive like HTML
            )
            Text(
                text = branch.address,
                fontSize = 14.sp,
                color = textSub,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(statusColor, CircleShape)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textSub
            )
        }
    }
}

data class Branch(
    val name: String,
    val address: String,
    val isActive: Boolean
)

@Preview
@Composable
fun PreviewBranchManagementScreen() {
    BranchManagementScreen()
}
