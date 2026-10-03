package com.hye.weatherwalk.data.remote

import com.hye.weatherwalk.data.remote.dto.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double= 37.5665,
        @Query("longitude") longitude: Double= 126.9780 ,
        @Query("past_days") pastDays: Int = 7,
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("timezone") timezone: String = "auto",
        @Query("hourly") hourly: List<String> = listOf("temperature_2m","weather_code","precipitation")
    ): WeatherDTO
}