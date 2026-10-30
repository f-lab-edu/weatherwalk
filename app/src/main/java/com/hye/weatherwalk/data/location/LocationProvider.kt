package com.hye.weatherwalk.data.location

import com.hye.weatherwalk.domain.model.Coordinates

interface LocationProvider {
    suspend fun getCurrentLocation(): Coordinates?
}