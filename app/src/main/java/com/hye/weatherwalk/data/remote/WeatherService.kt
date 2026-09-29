package com.hye.weatherwalk.data.remote


interface WeatherService {

   suspend fun getWeatherInfo()
}