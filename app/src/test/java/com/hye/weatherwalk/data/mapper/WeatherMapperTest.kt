package com.hye.weatherwalk.data.mapper

import com.hye.weatherwalk.data.local.HourlyWeatherEntity
import com.hye.weatherwalk.domain.model.WeatherCode
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import java.time.LocalDateTime

class WeatherMapperTest {

    @Test
    fun `엔티티의 값을 도메인으로 옮긴다`() {
        // Given
        val entity = HourlyWeatherEntity(
            time = "2026-10-15T09:00",
            latitude = 37.57,
            longitude = 126.93,
            temperature = 15.9,
            precipitation = 0.4,
            weatherCode = 61,
        )

        // When
        val domain = entity.toDomain()

        // Then
        assertEquals(LocalDateTime.of(2026, 10, 15, 9, 0), domain.time)
        assertEquals(15.9, domain.temperature!!, 0.0)
        assertEquals(0.4, domain.precipitation!!, 0.0)
        assertEquals(WeatherCode.RAIN, domain.weatherCode)
    }

    @Test
    fun `값이 없으면 null을 유지하고 날씨 코드는 UNKNOWN이 된다`() {
        // Given
        val entity = HourlyWeatherEntity(
            time = "2026-10-15T10:00",
            latitude = 37.57,
            longitude = 126.93,
            temperature = null,
            precipitation = null,
            weatherCode = null,
        )

        // When
        val domain = entity.toDomain()

        // Then
        assertNull(domain.temperature)
        assertNull(domain.precipitation)
        assertEquals(WeatherCode.UNKNOWN, domain.weatherCode)
    }

    @Test
    fun `리스트를 순서대로 변환한다`() {
        // Given
        val entities = listOf(
            HourlyWeatherEntity("2026-10-15T00:00", 37.57, 126.93, 10.0, 0.0, 0),
            HourlyWeatherEntity("2026-10-15T01:00", 37.57, 126.93, 11.0, 0.0, 3),
        )

        // When
        val result = entities.toDomain()

        // Then
        assertEquals(2, result.size)
        assertEquals(0, result[0].time.hour)
        assertEquals(WeatherCode.CLEAR, result[0].weatherCode)
        assertEquals(1, result[1].time.hour)
        assertEquals(WeatherCode.CLOUDY, result[1].weatherCode)
    }
}