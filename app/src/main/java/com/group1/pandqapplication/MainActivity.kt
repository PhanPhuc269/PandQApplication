package com.group1.pandqapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.group1.pandqapplication.ui.navigation.PandQNavGraph
import com.group1.pandqapplication.ui.theme.PandQApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PandQApplicationTheme {
                PandQNavGraph()
            }
        }
    }
}