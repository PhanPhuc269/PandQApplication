package com.group1.pandqapplication

import androidx.lifecycle.ViewModel
import com.group1.pandqapplication.data.repository.AuthRepository
import com.group1.pandqapplication.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getStartDestination(): String {
        return if (authRepository.isUserLoggedIn()) {
            Screen.Home.route
        } else {
            Screen.Onboarding.route
        }
    }
}
