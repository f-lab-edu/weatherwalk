package com.hye.weatherwalk.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hye.weatherwalk.data.location.LocationProvider
import com.hye.weatherwalk.domain.model.Coordinates
import com.hye.weatherwalk.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider,
) : ViewModel() {

    init {
        refresh()
    }

    private sealed interface RefreshStatus {
        data object Loading : RefreshStatus                     // 요청 중
        data object Done : RefreshStatus                        // 요청 완료 (성공)
        data class Failed(val message: String) : RefreshStatus  // 요청 완료 (실패)
    }

    private val refreshStatus = MutableStateFlow<RefreshStatus>(RefreshStatus.Loading)

    /*  val uiState: StateFlow<WeatherUiState> =
          weatherRepository.observeHourlyWeather()
              .map { list -> WeatherUiState.Success(list.map { it.toUi() }) }
              .stateIn(
                  scope = viewModelScope,
                  started = SharingStarted.WhileSubscribed(5000),
                  initialValue = WeatherUiState.Loading,
              )*/

    val uiState = combine(weatherRepository.observeHourlyWeather(), refreshStatus) { list, status ->
        when {
            list.isNotEmpty() -> WeatherUiState.Success(list.map { it.toUi() })
            status is RefreshStatus.Loading -> WeatherUiState.Loading
            status is RefreshStatus.Failed -> WeatherUiState.Error(status.message)
            else -> WeatherUiState.Empty     // status == Done
        }
    }

    /*
    * refresh() API 호출 → 파싱 → 매퍼 → Room 저장
    *
    * */

    fun refresh() {
        viewModelScope.launch {
            refreshStatus.value = RefreshStatus.Loading

            refreshStatus.value = try {
                val location = locationProvider.getCurrentLocation() ?: DEFAULT_LOCATION  // 서울시청
                weatherRepository.refreshWeather(location.latitude, location.longitude)
                RefreshStatus.Done
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                RefreshStatus.Failed(e.toUserMessage())
            }
        }
    }

/*    fun loadWeather() {
        viewModelScope.launch {
            try {
                val coordinates = locationProvider.getCurrentLocation() ?: DEFAULT_LOCATION
                weatherRepository.refreshWeather(coordinates.latitude, coordinates.longitude)
            } catch (e: Exception) {

            }
        }
    }*/

    companion object {
        private val DEFAULT_LOCATION = Coordinates(37.5665, 126.9780)
    }

    private fun Throwable.toUserMessage(): String = when (this) {
        is java.io.IOException -> "네트워크 연결을 확인해 주세요"
        is retrofit2.HttpException -> "날씨 서버에 문제가 발생했습니다 (${code()})"
        else -> "날씨 정보를 불러오지 못했습니다"
    }

}