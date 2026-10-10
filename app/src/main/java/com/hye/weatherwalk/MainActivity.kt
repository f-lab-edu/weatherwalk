package com.hye.weatherwalk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.hye.weatherwalk.data.remote.WeatherClient
import com.hye.weatherwalk.ui.theme.WeatherWalkTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 테스트용 호출
        lifecycleScope.launch {
            try {
                val result = WeatherClient.weatherService.getWeatherForecast(37.5665, 126.9780)
                Log.d("WeatherTest", "시간 개수: ${result.hourly.time.size}")
                Log.d("WeatherTest", "시간: ${result.hourly.time.take(5)}")
                Log.d("WeatherTest", "기온: ${result.hourly.temperature2m.take(5)}")
                Log.d("WeatherTest", "날씨코드: ${result.hourly.weatherCode.take(5)}")
            } catch (e: Exception) {
                Log.e("WeatherTest", "실패", e)
            }
        }
        setContent {
            WeatherWalkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherWalkTheme {
        Greeting("Android")
    }
}
