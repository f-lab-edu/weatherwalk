package com.hye.weatherwalk.presentation.today

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.hye.weatherwalk.ui.theme.WeatherWalkTheme

@Composable
fun TodayScreen(){
    Column{
           Text("TodayScreen")
}
    }


@Preview(showBackground = true)
@Composable
fun TodayScreenPreview() {
    WeatherWalkTheme {
        TodayScreen()
    }
}