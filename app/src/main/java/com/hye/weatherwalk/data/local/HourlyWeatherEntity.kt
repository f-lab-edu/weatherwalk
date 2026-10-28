package com.hye.weatherwalk.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(
    tableName = "hourly_weather",)
data class HourlyWeatherEntity(
    @PrimaryKey val time: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double?,
    val precipitation: Double?,
    val weatherCode: Int?
)
