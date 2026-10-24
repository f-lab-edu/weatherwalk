package com.hye.weatherwalk.presentation.weather

import com.hye.weatherwalk.domain.model.HourlyWeather

fun HourlyWeather.toUi(): HourlyWeatherUi = HourlyWeatherUi(
    timeText = "%02d시".format(time.hour),
    temperatureText = temperature?.let { "${it}°" } ?: "-",
    precipitationText = precipitation?.let { "${it}mm" } ?: "-",
    weatherText = weatherCode.description,
)