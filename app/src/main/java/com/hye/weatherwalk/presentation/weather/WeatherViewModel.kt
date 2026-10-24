package com.hye.weatherwalk.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.weatherwalk.data.location.LocationProvider
import com.hye.weatherwalk.domain.model.Coordinates
import com.hye.weatherwalk.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    /*
    * errorMessage -> null 의미
    * 1. 최초 진입 시(viewModel 생성 시) : 아무 작업도 안함(errorMessage 초기값)
    * 2. 첫 번째 loadWeather() 호출 시 : 초기값
    * 3. loadWeather()호출 실패 한 후 다시 시도할 때 : 이전 에러 메시지를 지움
    * */

    private val errorMessage = MutableStateFlow<String?>(null)

  /*  val uiState: StateFlow<WeatherUiState> =
        weatherRepository.observeHourlyWeather()
            .map { list -> WeatherUiState.Success(list.map { it.toUi() }) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = WeatherUiState.Loading,
            )*/

    val uiState: StateFlow<WeatherUiState> =
        combine(weatherRepository.observeHourlyWeather(), errorMessage) { list, error ->
            when {
                list.isNotEmpty() -> WeatherUiState.Success(list.map { it.toUi() })  // 데이터가 있으면 항상 보여줌
                error != null -> WeatherUiState.Error(error)                          // 데이터 없고 실패
                else -> WeatherUiState.Loading                                        // 데이터 없고 아직 진행 중
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WeatherUiState.Loading,
        )

    /*
    * refreshWeather() API 호출 → 파싱 → 매퍼 → Room 저장
    *
    * */
    fun loadWeather() {
        viewModelScope.launch {
            errorMessage.value = null
            val coordinates = locationProvider.getCurrentLocation() ?: DEFAULT_LOCATION
            try {
                weatherRepository.refreshWeather(coordinates.latitude, coordinates.longitude)
            } catch (e: Exception) {

                errorMessage.value = "날씨 정보를 불러오지 못했어요"
            }
        }
    }

    companion object {
        private val DEFAULT_LOCATION = Coordinates(37.5665, 126.9780)
    }

}