package com.hye.weatherwalk.data.remote.dto

enum class WeatherCode(
    val codes: Set<Int>,
    val description: String,
) {
    CLEAR(setOf(0), "맑음"),
    MAINLY_CLEAR(setOf(1), "대체로 맑음"),
    PARTLY_CLOUDY(setOf(2), "구름 조금"),
    OVERCAST(setOf(3), "흐림"),
    FOG(setOf(45, 48), "안개"),
    DRIZZLE(setOf(51, 53, 55), "이슬비"),
    FREEZING_DRIZZLE(setOf(56, 57), "어는 이슬비"),
    RAIN(setOf(61, 63, 65), "비"),
    FREEZING_RAIN(setOf(66, 67), "어는 비"),
    SNOW(setOf(71, 73, 75), "눈"),
    SNOW_GRAINS(setOf(77), "싸락눈"),
    RAIN_SHOWERS(setOf(80, 81, 82), "소나기"),
    SNOW_SHOWERS(setOf(85, 86), "눈 소나기"),
    THUNDERSTORM(setOf(95), "뇌우"),
    THUNDERSTORM_HAIL(setOf(96, 99), "우박을 동반한 뇌우"),
    UNKNOWN(emptySet(), "알 수 없음");
}