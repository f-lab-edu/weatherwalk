package com.hye.weatherwalk.presentation.main

import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val contentDescription: String
    ) {
    TODAY("today", "Today", "Today"),
    WEEK("week", "This Week", "week"),
    MAP("map", "Map", "Map")
}