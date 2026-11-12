package com.hye.weatherwalk.presentation.uistate

sealed interface HealthConnectUiState {
    object Loading : HealthConnectUiState
    object SdkUnavailable : HealthConnectUiState
}