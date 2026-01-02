package com.group1.pandqapplication.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.pandqapplication.shared.data.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferenceRepository.setFirstLaunchCompleted()
            onSuccess()
        }
    }
}
