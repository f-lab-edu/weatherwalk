package com.hye.weatherwalk.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


const val BASE_URL = "https://api.open-meteo.com/"

object WeatherClient {

    private val json = Json {
        // API 응답에 데이터 클래스에 정의되지 않은 키가 있어도 에러를 내지 않고 무시
        ignoreUnknownKeys = true

    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory(
                "application/json; charset=utf-8".toMediaType()
            )
        )
        .build()

    val weatherService = retrofit.create(WeatherService::class.java)

}