package com.hye.weatherwalk.data.remote

import com.hye.weatherwalk.data.remote.dto.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("past_days") pastDays: Int = 7,
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("hourly") hourly: List<String> = listOf("temperature_2m","weather_code","precipitation")
    ): WeatherDTO
}