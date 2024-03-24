package com.hedmer.anibreak.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hedmer.anibreak.common.util.Result
import com.hedmer.anibreak.domain.usecase.home.GetHomeMediaUseCase
import com.hedmer.anibreak.domain.usecase.home.RefreshHomeMediaUseCase
import com.hedmer.anibreak.home.HomeUiState.Loading
import com.hedmer.anibreak.model.HomeMedia
import com.hedmer.anibreak.testing.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private val homeMediaUseCase: GetHomeMediaUseCase =
    mockk(relaxed = true)

  private val refreshMediaUseCase: RefreshHomeMediaUseCase =
    mockk(relaxed = true)

  private lateinit var viewModel: HomeViewModel

  @Test
  fun `loading on init state`() {
    viewModel = createViewModel()
    assertThat(viewModel.state.value).isEqualTo(Loading)
  }

  @Test
  fun `refreshing as false on vm init`() {
    viewModel = createViewModel()
    assertThat(viewModel.isRefreshing).isFalse()
  }

  @Test
  fun `refreshing as true after invalidate`() = runTest {
    coEvery { refreshMediaUseCase() } coAnswers {
      delay(1000)
      Result.Success(HomeMedia.Empty)
    }

    viewModel = createViewModel()
    viewModel.invalidateHomeScreen()

    assertThat(viewModel.isRefreshing).isTrue()
    advanceUntilIdle()
    assertThat(viewModel.isRefreshing).isFalse()
  }

  @Test
  fun `set home success state when result error is returned`() = runTest {
    val homeMedia = HomeMedia.Empty
    every { homeMediaUseCase() } returns flowOf(Result.Success(homeMedia))

    viewModel = createViewModel()

    viewModel.state.test {
      assertThat(HomeUiState.Success(HomeAnimeContentState(homeMedia)))
        .isEqualTo(awaitItem())
    }
  }

  @Test
  fun `set home error state when result error is returned`() = runTest {
    val error = Throwable()
    every { homeMediaUseCase() } returns flowOf(Result.Error(error))

    viewModel = createViewModel()

    viewModel.state.test {
      assertThat(HomeUiState.Error).isEqualTo(awaitItem())
    }
  }

  private fun createViewModel() =
    HomeViewModel(
      homeMediaUseCase,
      refreshMediaUseCase
    )
}
