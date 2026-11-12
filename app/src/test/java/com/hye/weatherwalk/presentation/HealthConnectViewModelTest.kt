package com.hye.weatherwalk.presentation

import androidx.health.connect.client.HealthConnectClient
import com.hye.weatherwalk.presentation.uistate.HealthConnectUiState
import com.hye.weatherwalk.presentation.viewmodel.HealthConnectViewModel
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HealthConnectViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeHealthConnectRepository
    private lateinit var viewModel: HealthConnectViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeHealthConnectRepository()
    }

    @Test
    fun `SDK가_미지원일_때_UiState는_SdkUnavailable이다`() = runTest {
        // 1. Given (조건 설정)
        fakeRepository.sdkStatusResult = HealthConnectClient.SDK_UNAVAILABLE

        // 2. When (동작 수행)
        viewModel = HealthConnectViewModel(fakeRepository)

        // 3. Then (결과 검증)
        TestCase.assertEquals(HealthConnectUiState.SdkUnavailable, viewModel.uiState.value)
    }





}