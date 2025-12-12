package com.group1.pandqapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.group1.pandqapplication.ui.common.NetworkErrorScreen
import com.group1.pandqapplication.util.ConnectivityObserver
import com.group1.pandqapplication.ui.navigation.PandQNavGraph
import com.group1.pandqapplication.ui.theme.PandQApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Explicit call since import was likely removed by user

        val startDestination = mainViewModel.getStartDestination()
        
        
        
        setContent {
            val networkStatus by mainViewModel.networkStatus.collectAsState()
            PandQApplicationTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    val startDestination = mainViewModel.getStartDestination()
                    val destinationAfterSplash = mainViewModel.getNextDestination()
                    
                    PandQNavGraph(
                        startDestination = startDestination,
                        destinationAfterSplash = destinationAfterSplash
                    )

                    if (networkStatus == ConnectivityObserver.Status.Unavailable || 
                        networkStatus == ConnectivityObserver.Status.Lost) {
                       NetworkErrorScreen()
                    }
                }
            }
        }
    }
}