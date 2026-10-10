package com.hye.weatherwalk.data.mapper.local

import com.hye.weatherwalk.data.mapper.toEntities
import com.hye.weatherwalk.data.remote.dto.WeatherDTO
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertThrows
import org.junit.Test

class WeatherDtoParsingTest {

    private val json = Json { ignoreUnknownKeys = true }

    private val sampleResponse = """
        {
          "latitude": 37.55,
          "longitude": 127.0,
          "generationtime_ms": 0.05,
          "utc_offset_seconds": 32400,
          "timezone": "Asia/Seoul",
          "timezone_abbreviation": "KST",
          "elevation": 38.0,
          "hourly_units": {
            "time": "iso8601",
            "temperature_2m": "°C",
            "precipitation": "mm",
            "weather_code": "wmo code"
          },
          "hourly": {
            "time": ["2026-10-10T00:00", "2026-10-10T01:00", "2026-10-10T02:00"],
            "temperature_2m": [15.2, 14.8, 14.1],
            "precipitation": [0.0, 0.3, 1.2],
            "weather_code": [0, 61, 63]
          }
        }
    """.trimIndent()

    @Test
    fun `위도와 경도가 파싱된다`() {
        // given
        val raw = sampleResponse

        // when
        val dto = json.decodeFromString<WeatherDTO>(raw)

        // then
        assertEquals(37.55, dto.latitude, 0.0)
        assertEquals(127.0, dto.longitude, 0.0)
    }

    @Test
    fun `hourly 배열이 SerialName에 맞게 파싱된다`() {
        // given
        val raw = sampleResponse

        // when
        val hourly = json.decodeFromString<WeatherDTO>(raw).hourly

        // then: temperature_2m, weather_code처럼 이름이 다른 필드도 제대로 들어온다
        assertEquals(
            listOf("2026-10-10T00:00", "2026-10-10T01:00", "2026-10-10T02:00"),
            hourly.time
        )
        assertEquals(listOf(15.2, 14.8, 14.1), hourly.temperature2m)
        assertEquals(listOf(0.0, 0.3, 1.2), hourly.precipitation)
        assertEquals(listOf(0, 61, 63), hourly.weatherCode)
    }

    @Test
    fun `DTO에 없는 필드가 와도 파싱에 성공한다`() {
        // given: timezone, elevation, hourly_units 등 DTO에 없는 필드가 포함된 응답
        val raw = sampleResponse

        // when
        val dto = json.decodeFromString<WeatherDTO>(raw)

        // then: ignoreUnknownKeys 덕분에 에러 없이 필요한 값만 꺼낸다
        assertEquals(3, dto.hourly.time.size)
    }

    @Test
    fun `정수로 온 숫자도 Double로 파싱된다`() {
        // given: 기온이 15.0이 아니라 15로 온 경우
        val raw = """
            {
              "latitude": 37,
              "longitude": 127,
              "hourly": {
                "time": ["2026-10-10T00:00"],
                "temperature_2m": [15],
                "precipitation": [0],
                "weather_code": [0]
              }
            }
        """.trimIndent()

        // when
        val dto = json.decodeFromString<WeatherDTO>(raw)

        // then
        assertEquals(15.0, dto.hourly.temperature2m[0], 0.0)
    }

    @Test
    fun `필수 필드가 없으면 파싱에 실패한다`() {
        // given: hourly 안에 weather_code가 빠진 응답
        val raw = """
            {
              "latitude": 37.55,
              "longitude": 127.0,
              "hourly": {
                "time": ["2026-10-10T00:00"],
                "temperature_2m": [15.2],
                "precipitation": [0.0]
              }
            }
        """.trimIndent()

        // when & then: 조용히 넘어가지 않고 예외가 발생한다
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<WeatherDTO>(raw)
        }
    }

    @Test
    fun `배열 안에 null이 있으면 파싱에 실패한다`() {
        // given: 날씨 API는 데이터가 없는 시간에 null을 보내기도 한다
        val raw = """
            {
              "latitude": 37.55,
              "longitude": 127.0,
              "hourly": {
                "time": ["2026-10-10T00:00", "2026-10-10T01:00"],
                "temperature_2m": [15.2, null],
                "precipitation": [0.0, 0.3],
                "weather_code": [0, 61]
              }
            }
        """.trimIndent()

        // when & then
        // 정책: 현재 DTO는 List<Double>(non-null)이라 응답 전체가 실패한다.
        // null을 허용하기로 하면 DTO를 List<Double?>로 바꾸고 이 테스트를 수정할 것.
        assertThrows(SerializationException::class.java) {
            json.decodeFromString<WeatherDTO>(raw)
        }
    }

    @Test
    fun `실제 응답 JSON이 파싱과 mapper를 거쳐 Entity로 변환된다`() {
        // given
        val raw = sampleResponse

        // when: JSON → DTO → Entity
        val entities = json.decodeFromString<WeatherDTO>(raw).toEntities()

        // then: 3시간치가 시간별로 묶여 나온다
        assertEquals(3, entities.size)
        with(entities[1]) {
            assertEquals("2026-10-10T01:00", time)
            assertEquals(14.8, temperature, 0.0)
            assertEquals(0.3, precipitation, 0.0)
            assertEquals(61, weatherCode)
            assertEquals(37.55, latitude, 0.0)
        }
    }
}