package com.group1.pandqapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.group1.pandqapplication.R
import com.group1.pandqapplication.util.LocaleManager

@Composable
fun LanguagePickerDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val primaryColor = Color(0xFFec3713)
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.select_language),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Language Options
                LanguageOption(
                    flag = "🇺🇸",
                    name = stringResource(R.string.language_english),
                    isSelected = currentLanguage == LocaleManager.ENGLISH,
                    primaryColor = primaryColor,
                    onClick = { onLanguageSelected(LocaleManager.ENGLISH) }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LanguageOption(
                    flag = "🇻🇳",
                    name = stringResource(R.string.language_vietnamese),
                    isSelected = currentLanguage == LocaleManager.VIETNAMESE,
                    primaryColor = primaryColor,
                    onClick = { onLanguageSelected(LocaleManager.VIETNAMESE) }
                )
            }
        }
    }
}

@Composable
private fun LanguageOption(
    flag: String,
    name: String,
    isSelected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) primaryColor.copy(alpha = 0.1f) else Color(0xFFF3F4F6)
    val borderColor = if (isSelected) primaryColor else Color.Transparent
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, primaryColor) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag
            Text(
                text = flag,
                fontSize = 24.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Language Name
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = if (isSelected) primaryColor else Color(0xFF374151),
                modifier = Modifier.weight(1f)
            )
            
            // Checkmark
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
