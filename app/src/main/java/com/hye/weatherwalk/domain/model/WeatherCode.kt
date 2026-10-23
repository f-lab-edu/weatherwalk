package com.hye.weatherwalk.domain.model

enum class WeatherCode(
    val codes: Set<Int>,
    val description: String,
) {
    CLEAR(setOf(0, 1), "맑음"),
    CLOUDY(setOf(2, 3), "흐림"),
    FOG(setOf(45, 48), "안개"),
    RAIN((51..67).toSet() + (80..82), "비"),
    SNOW((71..77).toSet() + setOf(85, 86), "눈"),
    THUNDERSTORM((95..99).toSet(), "뇌우"),
    UNKNOWN(emptySet(), "알 수 없음");


    /*
  * 조건 있는 firstOrNull{} -> 조건에 만족하는 첫번째 값이 있으면 그 값을 반환하고(거기서 빠져나옴) , 마지막까지도 만족하는 값이 없으면 null반환
  * */
    companion object {
        fun from(code: Int?): WeatherCode =
            entries.firstOrNull { code in it.codes } ?: UNKNOWN
    }


}

