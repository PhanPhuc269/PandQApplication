package com.group1.pandqapplication.ui.guide
 
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.group1.pandqapplication.ui.onboarding.GuidePager
 
@Composable
fun UserGuideScreen(
    onBackClick: () -> Unit = {}
) {
    // Reuse the GuidePager in review mode
    GuidePager(
        onFinish = onBackClick,
        isReviewMode = true
    )
}
 
@Preview
@Composable
fun PreviewUserGuideScreen() {
    UserGuideScreen()
}
