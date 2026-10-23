package com.hye.weatherwalk.data.mapper

import com.hye.weatherwalk.data.local.HourlyWeatherEntity
import com.hye.weatherwalk.data.remote.dto.WeatherDTO
import com.hye.weatherwalk.domain.model.HourlyWeather
import com.hye.weatherwalk.domain.model.WeatherCode
import java.time.LocalDateTime

/*
* 시간별 항목들 중 가장 작은 사이즈 추출
* */
fun WeatherDTO.toEntities(lat: Double, lon: Double): List<HourlyWeatherEntity> {
    val hourly = hourly ?: return emptyList()

    return hourly.time.mapIndexed { i, t ->
        HourlyWeatherEntity(
            time = t,
            latitude = lat,
            longitude = lon,
            temperature = hourly.temperature2m.getOrNull(i),
            precipitation = hourly.precipitation.getOrNull(i),
            weatherCode = hourly.weatherCode.getOrNull(i),
        )
    }
}


/*    val size = minOf(
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
    }*/

/*fun HourlyWeatherEntity.toDomain(): HourlyWeather= HourlyWeather(
    time = LocalDateTime.parse(time),
    temperature = temperature,
    precipitation = precipitation,
    weatherCode = WeatherCode.from(weatherCode)
)*/
fun HourlyWeatherEntity.toDomain(): HourlyWeather = HourlyWeather(
    time = LocalDateTime.parse(time),
    temperature = temperature,
    precipitation = precipitation,
    weatherCode = WeatherCode.from(weatherCode)
)

fun List<HourlyWeatherEntity>.toDomain(): List<HourlyWeather> = map { it.toDomain() }


