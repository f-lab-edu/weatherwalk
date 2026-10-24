package com.hye.weatherwalk.presentation.weather

import com.hye.weatherwalk.domain.model.HourlyWeather
import com.hye.weatherwalk.domain.model.WeatherCode
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class WeatherUiMapperTest {
    @Test
    fun `값이 있으면 단위를 붙여 표시한다`() {
        // Given
        val weather = HourlyWeather(
            time = LocalDateTime.of(2026, 10, 24, 9, 0),
            precipitation = 0.4,
            temperature = 15.9,
            weatherCode = WeatherCode.RAIN,
        )

        // When
        val ui = weather.toUi()

        // Then
        assertEquals("09시", ui.timeText)
        assertEquals("15.9°", ui.temperatureText)
        assertEquals("0.4mm", ui.precipitationText)
        assertEquals("비", ui.weatherText)
    }

    @Test
    fun `값이 0이면 하이픈이 아니라 0으로 표시한다`() {
        // Given
        val weather = HourlyWeather(
            time = LocalDateTime.of(2026, 10, 24, 0, 0),
            precipitation = 0.0,
            temperature = 0.0,
            weatherCode = WeatherCode.CLEAR,
        )

        // When
        val ui = weather.toUi()

        // Then
        assertEquals("00시", ui.timeText)
        assertEquals("0.0°", ui.temperatureText)
        assertEquals("0.0mm", ui.precipitationText)
    }

    @Test
    fun `값이 없으면 하이픈과 알 수 없음으로 표시한다`() {
        // Given
        val weather = HourlyWeather(
            time = LocalDateTime.of(2026, 10, 24, 23, 0),
            precipitation = null,
            temperature = null,
            weatherCode = WeatherCode.UNKNOWN,
        )

        // When
        val ui = weather.toUi()

        // Then
        assertEquals("23시", ui.timeText)
        assertEquals("-", ui.temperatureText)
        assertEquals("-", ui.precipitationText)
        assertEquals("알 수 없음", ui.weatherText)
    }
}