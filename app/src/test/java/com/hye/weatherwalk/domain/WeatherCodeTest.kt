package com.hye.weatherwalk.domain

import com.hye.weatherwalk.domain.model.WeatherCode
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeatherCodeTest {
    @Test
    fun `코드가 속한 그룹을 찾는다`() {
        // Given: 각 그룹에 속하는 날씨 코드
        val clearCode = 0
        val cloudyCode = 3
        val fogCode = 45
        val snowCode = 85
        val thunderstormCode = 97

        // When: from()으로 그룹을 찾음
        val clear = WeatherCode.from(clearCode)
        val cloudy = WeatherCode.from(cloudyCode)
        val fog = WeatherCode.from(fogCode)
        val snow = WeatherCode.from(snowCode)
        val thunderstorm = WeatherCode.from(thunderstormCode)

        // Then: 의도한 그룹이 반환됨
        assertEquals(WeatherCode.CLEAR, clear)
        assertEquals(WeatherCode.CLOUDY, cloudy)
        assertEquals(WeatherCode.FOG, fog)
        assertEquals(WeatherCode.SNOW, snow)
        assertEquals(WeatherCode.THUNDERSTORM, thunderstorm)
    }

    @Test
    fun `이슬비, 비, 소나기는 모두 RAIN이다`() {
        // Given: 비 계열 코드 (각 범위의 양 끝 포함)
        val rainCodes = listOf(51, 57, 61, 67, 80, 82)

        // When: 각 코드를 그룹으로 변환
        val results = rainCodes.map { WeatherCode.from(it) }

        // Then: 모두 RAIN
        results.forEachIndexed { i, result ->
            assertEquals("code=${rainCodes[i]}", WeatherCode.RAIN, result)
        }
    }

    @Test
    fun `null이나 정의되지 않은 코드는 UNKNOWN이다`() {
        // Given: 어느 그룹에도 속하지 않는 입력
        val nullCode: Int? = null
        val undefinedCode = 999
        val negativeCode = -1

        // When
        val fromNull = WeatherCode.from(nullCode)
        val fromUndefined = WeatherCode.from(undefinedCode)
        val fromNegative = WeatherCode.from(negativeCode)

        // Then: 모두 UNKNOWN
        assertEquals(WeatherCode.UNKNOWN, fromNull)
        assertEquals(WeatherCode.UNKNOWN, fromUndefined)
        assertEquals(WeatherCode.UNKNOWN, fromNegative)
    }

}