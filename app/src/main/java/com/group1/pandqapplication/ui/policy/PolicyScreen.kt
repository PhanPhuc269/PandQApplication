package com.group1.pandqapplication.ui.policy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group1.pandqapplication.shared.ui.theme.PolicyBorderDark
import com.group1.pandqapplication.shared.ui.theme.PolicyBorderLight
import com.group1.pandqapplication.shared.ui.theme.PolicyComponentDark
import com.group1.pandqapplication.shared.ui.theme.PolicyComponentLight
import com.group1.pandqapplication.shared.ui.theme.PolicySegmentDark
import com.group1.pandqapplication.shared.ui.theme.PolicySegmentLight
import com.group1.pandqapplication.shared.ui.theme.PolicyTextPrimaryDark
import com.group1.pandqapplication.shared.ui.theme.PolicyTextPrimaryLight
import com.group1.pandqapplication.shared.ui.theme.PolicyTextSecondaryDark
import com.group1.pandqapplication.shared.ui.theme.PolicyTextSecondaryLight
import com.group1.pandqapplication.shared.ui.theme.ProductPrimary
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundDark
import com.group1.pandqapplication.shared.ui.theme.RoleBackgroundLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(
    onBackClick: () -> Unit = {}
) {
    val isDarkTheme = false
    
    val backgroundColor = if (isDarkTheme) RoleBackgroundDark else RoleBackgroundLight
    val componentColor = if (isDarkTheme) PolicyComponentDark else PolicyComponentLight
    val segmentColor = if (isDarkTheme) PolicySegmentDark else PolicySegmentLight
    val borderColor = if (isDarkTheme) PolicyBorderDark else PolicyBorderLight
    val textPrimary = if (isDarkTheme) PolicyTextPrimaryDark else PolicyTextPrimaryLight
    val textSecondary = if (isDarkTheme) PolicyTextSecondaryDark else PolicyTextSecondaryLight

    var selectedTab by remember { androidx.compose.runtime.mutableIntStateOf(0) }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Column(modifier = Modifier.background(componentColor).statusBarsPadding()) {
                TopAppBar(
                    title = {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                             Text(
                                 text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_screen_title),
                                 fontWeight = FontWeight.Bold,
                                 fontSize = 18.sp,
                                 color = textPrimary
                             )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Back", tint = ProductPrimary)
                        }
                    },
                    actions = {
                        Spacer(modifier = Modifier.width(48.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = componentColor)
                )
                HorizontalDivider(color = borderColor)
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            val privacyTitle = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.tab_privacy_policy)
            val termsTitle = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.tab_terms_service)

            // Custom Tab Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(segmentColor)
                    .padding(4.dp)
            ) {
                val options = listOf(privacyTitle, termsTitle)
                options.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) ProductPrimary else Color.Transparent)
                            .clickable { selectedTab = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else textPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                 // Meta info
                Text(
                    text = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.last_updated_fmt, "24/10/2024"),
                    color = textSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (selectedTab == 0) {
                    // Privacy Policy
                    AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_1),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_1),
                        isOpenDefault = true,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_2),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_2),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_3),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_3),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_4),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_4),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                } else {
                    // Terms
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_5),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_5),
                        isOpenDefault = true,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_6),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_6),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = borderColor
                    )
                     AccordionItem(
                        title = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_section_7),
                        content = androidx.compose.ui.res.stringResource(com.group1.pandqapplication.R.string.policy_content_7),
                        textPrimary = textPrimary,
                        textSecondary = textSecondary,
                        borderColor = Color.Transparent
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun AccordionItem(
    title: String,
    content: String,
    isOpenDefault: Boolean = false,
    textPrimary: Color,
    textSecondary: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(isOpenDefault) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = textSecondary,
                modifier = Modifier.rotate(if (expanded) 180f else 0f)
            )
        }
        
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
             Text(
                text = content,
                fontSize = 14.sp,
                color = textSecondary,
                lineHeight = 20.sp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
        
        HorizontalDivider(color = borderColor)
    }
}


@Preview
@Composable
fun PreviewPolicyScreen() {
    PolicyScreen()
}
