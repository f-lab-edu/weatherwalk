package com.hye.weatherwalk.data.mapper

import com.hye.weatherwalk.data.local.HourlyWeatherEntity
import com.hye.weatherwalk.data.remote.dto.WeatherDTO
/*
* 시간별 항목들 중 가장 작은 사이즈 추출
* */
fun WeatherDTO.toEntities(): List<HourlyWeatherEntity>{
    val size = minOf(
        hourly.time.size,
        hourly.temperature2m.size,
        hourly.precipitation.size,
        hourly.weatherCode.size
    )
    return List(size){ index ->
        HourlyWeatherEntity(
            latitude = latitude,
            longitude = longitude,
            time = hourly.time[index],
            temperature = hourly.temperature2m[index],
            precipitation = hourly.precipitation[index],
            weatherCode = hourly.weatherCode[index]
        )
    }
}