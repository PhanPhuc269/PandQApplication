package com.group1.pandqapplication.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.data.repository.AppRepository
import com.group1.pandqapplication.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState.asStateFlow()

    init {
        initializeApp()
    }

    private fun initializeApp() {
        viewModelScope.launch {
            appRepository.initializeApp().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = SplashState.Loading
                    is Result.Success -> _uiState.value = SplashState.Success
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
    object Success : SplashState()
    data class Error(val message: String) : SplashState()
}
