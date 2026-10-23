package com.hye.weatherwalk.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDTO(
    val latitude: Double,
    val longitude: Double,
    val hourly : HourlyWeatherDTO? = null
)

@Serializable
data class HourlyWeatherDTO(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperature2m : List<Double?> = emptyList(),
    val precipitation: List<Double?> = emptyList(),
    @SerialName("weather_code")val weatherCode: List<Int?> = emptyList()
)
