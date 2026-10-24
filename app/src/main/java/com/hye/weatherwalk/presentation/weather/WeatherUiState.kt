package com.hye.weatherwalk.presentation.weather

sealed interface WeatherUiState {
    data object Loading : WeatherUiState
    data class Success(val items: List<HourlyWeatherUi>) : WeatherUiState
    data class Error(val message: String) : WeatherUiState

}

data class HourlyWeatherUi(
    val timeText: String,
    val temperatureText: String,
    val precipitationText: String,
    val weatherText: String,
)