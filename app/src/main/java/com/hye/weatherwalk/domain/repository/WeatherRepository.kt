package com.hye.weatherwalk.domain.repository

import com.hye.weatherwalk.domain.model.HourlyWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun refreshWeather(latitude: Double, longitude: Double)
    fun observeHourlyWeather(): Flow<List<HourlyWeather>>
}