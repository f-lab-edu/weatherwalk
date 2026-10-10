package com.hye.weatherwalk.data.mapper

import com.hye.weatherwalk.data.local.HourlyWeatherEntity
import com.hye.weatherwalk.data.remote.dto.HourlyWeatherDTO
import com.hye.weatherwalk.data.remote.dto.WeatherDTO
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test


/*
* 검증할 케이스 체크리스트

1.시간 개수만큼 Entity가 생성되는지
2.같은 index의 time, 기온, 강수량, weatherCode가 한 Entity로 묶이는지
3.위경도가 모든 Entity에 복사되는지
4.배열 길이가 서로 다르면 가장 짧은 길이에 맞춰지는지
5.빈 배열이면 빈 리스트가 나오는지
* */

class WeatherDTOMapperTest {

    // 테스트용 DTO를 만드는 헬퍼 (테스트 데이터 팩토리)
    // 기본값이 있어서 각 테스트에서 바꾸고 싶은 값만 넘기면 된다.
    private fun dto(
        latitude: Double = 37.56,
        longitude: Double = 126.97,
        time: List<String> = listOf("2026-10-10T00:00", "2026-10-10T01:00"),
        temperature: List<Double> = listOf(15.2, 14.8),
        precipitation: List<Double> = listOf(0.0, 0.3),
        weatherCode: List<Int> = listOf(0, 61),
    ) = WeatherDTO(
        latitude = latitude,
        longitude = longitude,
        hourly = HourlyWeatherDTO(
            time = time,
            temperature2m = temperature,
            precipitation = precipitation,
            weatherCode = weatherCode
        )
    )

    @Test
    fun `시간 개수만큼 Entity가 생성된다`() {
        // given: 2시간치 데이터가 담긴 DTO
        val dto = dto()

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: Entity가 2개 생성된다
        assertEquals(2, entities.size)
    }

    @Test
    fun `같은 index의 값끼리 하나의 Entity로 묶인다`() {
        // given: 2시간치 데이터가 담긴 DTO와 기대하는 결과
        val dto = dto()
        val expected = listOf(
            HourlyWeatherEntity(
                latitude = 37.56,
                longitude = 126.97,
                time = "2026-10-10T00:00",
                temperature = 15.2,
                precipitation = 0.0,
                weatherCode = 0
            ),
            HourlyWeatherEntity(
                latitude = 37.56,
                longitude = 126.97,
                time = "2026-10-10T01:00",
                temperature = 14.8,
                precipitation = 0.3,
                weatherCode = 61
            )
        )

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: index별로 값이 묶인 Entity 리스트가 나온다
        assertEquals(expected, entities)
    }

    @Test
    fun `위경도가 모든 Entity에 복사된다`() {
        // given: 부산 좌표를 가진 DTO
        val dto = dto(latitude = 35.1, longitude = 129.04)

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: 모든 Entity에 같은 위경도가 들어간다
        entities.forEach {
            assertEquals(35.1, it.latitude, 0.0)
            assertEquals(129.04, it.longitude, 0.0)
        }
    }

    @Test
    fun `시간 순서가 API 응답 순서와 같다`() {
        // given: 3시간치 데이터가 담긴 DTO
        val time = listOf("2026-10-10T00:00", "2026-10-10T01:00", "2026-10-10T02:00")
        val dto = dto(
            time = time,
            temperature = listOf(1.0, 2.0, 3.0),
            precipitation = listOf(0.0, 0.0, 0.0),
            weatherCode = listOf(0, 0, 0)
        )

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: 응답 순서 그대로 시간이 정렬되어 있다
        assertEquals(time, entities.map { it.time })
    }

    @Test
    fun `배열 길이가 다르면 가장 짧은 길이에 맞춘다`() {
        // given: weatherCode만 1개인 DTO
        val dto = dto(weatherCode = listOf(0))

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: 1개만 생성되고, 첫 번째 시간 값이 들어간다
        assertEquals(1, entities.size)
        assertEquals("2026-10-10T00:00", entities[0].time)
    }

    /*
    정책: 날씨 정보 중 하나라도 없으면 해당 시간은 저장하지 않는다.
    (필수/선택 필드를 나누기로 하면 이 테스트를 수정할 것)
*
* */

    @Test
    fun `배열 중 하나라도 비어 있으면 빈 리스트를 반환한다`() {
        // given: precipitation만 비어 있는 DTO
        val dto = dto(precipitation = emptyList())

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: 빈 리스트가 반환된다
        assertTrue(entities.isEmpty())
    }

    @Test
    fun `모든 배열이 비어 있으면 빈 리스트를 반환한다`() {
        // given: 모든 배열이 비어 있는 DTO
        val dto = dto(
            time = emptyList(),
            temperature = emptyList(),
            precipitation = emptyList(),
            weatherCode = emptyList()
        )

        // when: Entity로 변환하면
        val entities = dto.toEntities()

        // then: 빈 리스트가 반환된다
        assertTrue(entities.isEmpty())
    }
}