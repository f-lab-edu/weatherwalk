package com.hye.weatherwalk.data.repositoryImpl

import com.hye.weatherwalk.data.local.WeatherDao
import com.hye.weatherwalk.data.mapper.toDomain
import com.hye.weatherwalk.data.mapper.toEntities
import com.hye.weatherwalk.data.remote.WeatherService
import com.hye.weatherwalk.domain.model.HourlyWeather
import com.hye.weatherwalk.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/*
* refreshWeather(): dto > Room
* observeHourlyWeather() -> Room-> domain : HourlyWeather
*
* */
class WeatherRepositoryImpl(
    private val service: WeatherService,
    private val dao: WeatherDao,
) : WeatherRepository {

    override suspend fun refreshWeather(lat: Double, lon: Double) {
        val dto = service.getWeatherForecast(lat, lon)
        val entities = dto.toEntities(lat, lon)
        dao.insertAll(entities)
    }

    override fun observeHourlyWeather(): Flow<List<HourlyWeather>> {
        return dao.observeHourly().map { list -> list.map { it.toDomain() } }
    }

}