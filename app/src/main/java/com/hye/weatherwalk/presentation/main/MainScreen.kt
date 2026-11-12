package com.hye.weatherwalk.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hye.weatherwalk.presentation.map.MapScreen
import com.hye.weatherwalk.presentation.today.TodayScreen
import com.hye.weatherwalk.presentation.week.WeekScreen
import com.hye.weatherwalk.ui.theme.WeatherWalkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()
    val startDestination = Destination.TODAY
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = Modifier,
        topBar = {
            Column {
                TopAppBar(title = { Text("WeatherWalk") })
                PrimaryTabRow(
                    selectedTabIndex = selectedDestination,
                    modifier = Modifier.padding()
                ) {
                    Destination.entries.forEachIndexed { index, destination ->
                        Tab(
                            selected = selectedDestination == index,
                            onClick = {
                                navController.navigate(route = destination.route)
                                selectedDestination = index
                            },
                            text = {
                                Text(
                                    text = destination.label,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        )

                    }
                }

            }

        }
    ) { contentPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable(Destination.TODAY.route) { TodayScreen() }
            composable(Destination.WEEK.route) { WeekScreen() }
            composable(Destination.MAP.route) { MapScreen() }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    WeatherWalkTheme {
        MainScreen()
    }
}