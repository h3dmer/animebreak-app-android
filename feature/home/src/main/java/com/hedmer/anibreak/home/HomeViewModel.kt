package com.hedmer.anibreak.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hedmer.anibreak.common.util.Result
import com.hedmer.anibreak.domain.usecase.home.GetHomeMediaUseCase
import com.hedmer.anibreak.domain.usecase.home.RefreshHomeMediaUseCase
import com.hedmer.anibreak.model.BasicMediaDetails
import com.hedmer.anibreak.model.HomeMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  getHomeMedia: GetHomeMediaUseCase,
  private val refreshMedia: RefreshHomeMediaUseCase
) : ViewModel() {

  val state: StateFlow<HomeUiState> =
    getHomeMedia()
      .map(::handleHomeUiState)
      .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 1_000),
        initialValue = HomeUiState.Loading,
      )

  var isRefreshing: Boolean by mutableStateOf(false)
    private set

  fun invalidateHomeScreen() {
    isRefreshing = true
    viewModelScope.launch {
      refreshMedia()
        .let {
          isRefreshing = false
        }
    }
  }

  private fun handleHomeUiState(
    result: Result<HomeMedia>
  ): HomeUiState {
    return when (result) {
      is Result.Success -> {
        HomeUiState.Success(
          HomeAnimeContentState(
            result.data
          )
        )
      }

      is Result.Error -> {
        HomeUiState.Error
      }
    }
  }
}

sealed interface HomeUiState {
  data object Loading : HomeUiState
  data object Error : HomeUiState
  data class Success(val homeAnimeData: HomeAnimeContentState) : HomeUiState
}

@Immutable
data class HomeAnimeContentState(
  val trendingNow: ImmutableList<BasicMediaDetails>,
  val popularThisSeason: ImmutableList<BasicMediaDetails>,
  val upcomingNextSeason: ImmutableList<BasicMediaDetails>,
  val allTimePopular: ImmutableList<BasicMediaDetails>,
  val topTen: ImmutableList<BasicMediaDetails>
) {

  constructor(homeMedia: HomeMedia): this(
    homeMedia.trendingNow.toImmutableList(),
    homeMedia.popularThisSeason.toImmutableList(),
    homeMedia.upcomingNextSeason.toImmutableList(),
    homeMedia.allTimePopular.toImmutableList(),
    homeMedia.topTen.toImmutableList()
  )
}
