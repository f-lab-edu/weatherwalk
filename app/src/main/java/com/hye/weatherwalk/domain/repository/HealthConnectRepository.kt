package com.hye.weatherwalk.domain.repository

interface HealthConnectRepository {
    fun getSdkStatus(): Int
    suspend fun hasAllPermissions(permissions: Set<String>): Boolean
    suspend fun getTodaySteps(): Long
}