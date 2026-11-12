package com.hye.weatherwalk.presentation.viewmodel

import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import com.hye.weatherwalk.domain.repository.HealthConnectRepository
import com.hye.weatherwalk.presentation.uistate.HealthConnectUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HealthConnectViewModel(
    private val repository: HealthConnectRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HealthConnectUiState>(HealthConnectUiState.Loading)
    val uiState: StateFlow<HealthConnectUiState> = _uiState.asStateFlow()

    init {
        checkStatus()
    }

    fun checkStatus() {
        if (repository.getSdkStatus() == HealthConnectClient.SDK_UNAVAILABLE) {
            _uiState.value = HealthConnectUiState.SdkUnavailable
        }
    }


}