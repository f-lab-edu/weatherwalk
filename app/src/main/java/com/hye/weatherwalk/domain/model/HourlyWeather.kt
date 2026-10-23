package com.hye.weatherwalk.domain.model


import java.time.LocalDateTime

data class HourlyWeather(
    val time: LocalDateTime,
    val precipitation: Double?,
    val temperature: Double?,
    val weatherCode: WeatherCode
)
