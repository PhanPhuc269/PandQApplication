package com.group1.pandqapplication.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.repository.AppRepository
import com.group1.pandqapplication.shared.data.repository.PreferenceRepository
import com.group1.pandqapplication.shared.util.ConnectivityObserver
import com.group1.pandqapplication.shared.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            // Combine app initialization with preference check
            // For simplicity, we just wait for app init then check preference
            appRepository.initializeApp().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = SplashState.Loading
                    is Result.Success -> {
                        val isFirstLaunch = preferenceRepository.isFirstLaunch.first()
                        _uiState.value = SplashState.Success(isFirstLaunch)
                    }
                    is Result.Error -> _uiState.value = SplashState.Error(result.message)
                }
            }
        }
    }

    fun retry() {
        initializeApp()
    }
}

sealed class SplashState {
    object Loading : SplashState()
    data class Success(val isFirstLaunch: Boolean) : SplashState()
    data class Error(val message: String) : SplashState()
}
