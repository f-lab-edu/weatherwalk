package com.hye.weatherwalk.presentation

import androidx.health.connect.client.HealthConnectClient
import com.hye.weatherwalk.domain.repository.HealthConnectRepository

class FakeHealthConnectRepository: HealthConnectRepository {

    var sdkStatusResult: Int = HealthConnectClient.SDK_AVAILABLE
    var hasPermissionResult: Boolean = true
    var todayStepsResult: Long = 5000L

    override fun getSdkStatus() = sdkStatusResult
    override suspend fun hasAllPermissions(permissions: Set<String>) = hasPermissionResult
    override suspend fun getTodaySteps() = todayStepsResult
}