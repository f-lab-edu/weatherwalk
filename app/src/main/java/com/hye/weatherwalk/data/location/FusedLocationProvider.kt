package com.hye.weatherwalk.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.hye.weatherwalk.domain.model.Coordinates
import kotlinx.coroutines.tasks.await

class FusedLocationProvider(
    private val context: Context,
) : LocationProvider {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getCurrentLocation(): Coordinates? {
        if (!hasCoarsePermission()) return null

        return try {
            val location = client
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
                .await()
                ?: client.lastLocation.await()
            location?.let { Coordinates(it.latitude, it.longitude) }
        } catch (e: SecurityException) {
            null
        }
    }

    private fun hasCoarsePermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}