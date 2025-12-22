package com.group1.pandqapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.repository.AuthRepository
import com.group1.pandqapplication.ui.navigation.Screen
import com.group1.pandqapplication.shared.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val networkStatus: StateFlow<ConnectivityObserver.Status> = connectivityObserver.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Available)

    fun getStartDestination(): String {
        return Screen.Splash.route
    }

    fun getNextDestination(): String {
        return if (authRepository.isUserLoggedIn()) {
            Screen.Home.route
        } else {
            Screen.Onboarding.route
        }
    }

    fun logout() {
        authRepository.logout()
    }
}
