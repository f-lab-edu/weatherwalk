package com.hye.weatherwalk.data.local

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    /**
     * 시간별 날씨를 한 번에 저장한다.
     * 같은 (latitude, longitude, time)이 이미 있으면 새 값으로 덮어쓴다.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<HourlyWeatherEntity>)

    /**
     * 특정 위치의 시간별 날씨를 시간순으로 관찰한다.
     * DB가 바뀔 때마다 새 리스트가 흘러나온다.
     */
    @Query(
        """
        SELECT * FROM hourly_weather
        WHERE latitude = :latitude AND longitude = :longitude
        ORDER BY time ASC
        """
    )
    fun observeHourly(latitude: Double, longitude: Double): Flow<List<HourlyWeatherEntity>>

    /**
     * 기준 시간 이전의 오래된 데이터를 삭제한다.
     * time은 ISO 문자열("2026-10-10T15:00")이라 문자열 비교로 시간 비교가 된다.
     */
    @Query("DELETE FROM hourly_weather WHERE time < :time")
    suspend fun deleteBefore(time: String)

}