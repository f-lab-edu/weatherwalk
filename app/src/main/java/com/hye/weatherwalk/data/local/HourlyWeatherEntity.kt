package com.hye.weatherwalk.data.local

import androidx.room3.Entity

@Entity(
    "hourly_weather",
    primaryKeys = ["latitude", "longitude", "time"]

)
data class HourlyWeatherEntity(
    val latitude: Double,
    val longitude: Double,
    val time: String,
    val temperature: Double,
    val precipitation: Double,
    val weatherCode: Int,
)
