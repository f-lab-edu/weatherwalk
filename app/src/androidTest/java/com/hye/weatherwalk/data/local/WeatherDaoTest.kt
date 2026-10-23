package com.hye.weatherwalk.data.local

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherDaoTest {

    // @Before에서 반드시 먼저 초기화되므로 lateinit이 적합
    private lateinit var db: AppDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setUp() {
        // 테스트마다 새로운 인메모리 DB: 파일에 저장되지 않고, 테스트 간에 데이터가 섞이지 않음
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(
            context = ApplicationProvider.getApplicationContext()
        )
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.weatherDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    // 테스트 데이터 팩토리: 바꿀 값만 넘기면 된다
    private fun entity(
        time: String,
        latitude: Double = 37.56,
        longitude: Double = 126.97,
        temperature: Double = 15.0,
        precipitation: Double = 0.0,
        weatherCode: Int = 0
    ) = HourlyWeatherEntity(
        latitude = latitude,
        longitude = longitude,
        time = time,
        temperature = temperature,
        precipitation = precipitation,
        weatherCode = weatherCode
    )

    // ---------- insertAll ----------

    @Test
    fun 저장한_데이터가_그대로_조회된다() = runTest {
        // given
        val entities = listOf(
            entity(time = "2026-10-10T00:00", temperature = 15.2),
            entity(time = "2026-10-10T01:00", temperature = 14.8)
        )

        // when
        dao.insertAll(entities)
        val result = dao.observeHourly(37.56, 126.97).first()

        // then
        assertEquals(entities, result)
    }

    @Test
    fun 같은_위치와_시간을_다시_저장하면_덮어쓴다() = runTest {
        // given: 15.0도로 저장된 데이터
        dao.insertAll(listOf(entity(time = "2026-10-10T00:00", temperature = 15.0)))

        // when: 같은 키로 20.0도를 다시 저장
        dao.insertAll(listOf(entity(time = "2026-10-10T00:00", temperature = 20.0)))
        val result = dao.observeHourly(37.56, 126.97).first()

        // then: 개수는 1개 그대로, 값만 바뀐다
        assertEquals(1, result.size)
        assertEquals(20.0, result[0].temperature, 0.0)
    }

    // ---------- observeHourly ----------

    @Test
    fun 요청한_위치의_데이터만_조회된다() = runTest {
        // given: 서울과 부산 데이터
        dao.insertAll(
            listOf(
                entity(time = "2026-10-10T00:00", latitude = 37.56, longitude = 126.97),
                entity(time = "2026-10-10T00:00", latitude = 35.1, longitude = 129.04)
            )
        )

        // when: 서울만 조회
        val result = dao.observeHourly(37.56, 126.97).first()

        // then: 서울 데이터 1개만 나온다
        assertEquals(1, result.size)
        assertEquals(37.56, result[0].latitude, 0.0)
    }

    @Test
    fun 시간_순서를_섞어서_저장해도_오름차순으로_조회된다() = runTest {
        // given: 순서를 섞어서 저장
        dao.insertAll(
            listOf(
                entity(time = "2026-10-10T02:00"),
                entity(time = "2026-10-10T00:00"),
                entity(time = "2026-10-10T01:00")
            )
        )

        // when
        val result = dao.observeHourly(37.56, 126.97).first()

        // then: 00시 → 01시 → 02시
        assertEquals(
            listOf("2026-10-10T00:00", "2026-10-10T01:00", "2026-10-10T02:00"),
            result.map { it.time }
        )
    }

    @Test
    fun 데이터가_없으면_빈_리스트가_조회된다() = runTest {
        // given: 아무것도 저장하지 않음

        // when
        val result = dao.observeHourly(37.56, 126.97).first()

        // then
        assertTrue(result.isEmpty())
    }

    // ---------- deleteBefore ----------

    @Test
    fun 기준_시간_이전_데이터만_삭제된다() = runTest {
        // given: 00시, 01시, 02시 데이터
        dao.insertAll(
            listOf(
                entity(time = "2026-10-10T00:00"),
                entity(time = "2026-10-10T01:00"),
                entity(time = "2026-10-10T02:00")
            )
        )

        // when: 01시 이전 삭제
        dao.deleteBefore("2026-10-10T01:00")
        val result = dao.observeHourly(37.56, 126.97).first()

        // then: 00시만 지워지고, 기준 시간(01시)과 그 이후는 남는다
        assertEquals(
            listOf("2026-10-10T01:00", "2026-10-10T02:00"),
            result.map { it.time }
        )
    }
}