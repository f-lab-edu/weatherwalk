package com.hye.weatherwalk.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
    val latitude: Double,
    val longitude: Double,
    val hourly : HourlyWeatherDTO
)

@Serializable
data class HourlyWeatherDTO(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature2m : List<Double>,
    val precipitation: List<Double>,
    @SerialName("weather_code")val weatherCode: WeatherCode
)
