package com.hedmer.anibreak.animedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hedmer.anibreak.domain.mapper.DomainResult
import com.hedmer.anibreak.domain.usecase.mediadetails.GetMediaDetailsUseCase
import com.hedmer.anibreak.model.MediaDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MediaDetailsViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val getMediaDetailsUseCase: GetMediaDetailsUseCase,
  private val uiStateFactory: MediaDetailsUiStateFactory
) : ViewModel() {

  private val _uiState: MutableStateFlow<MediaDetailsUiState> =
    MutableStateFlow(MediaDetailsUiState.Loading)
  val uiState: MutableStateFlow<MediaDetailsUiState>
    get() = _uiState

  fun processIntent(intent: MediaDetailsIntent) {
    when (intent) {
      MediaDetailsIntent.OnScreenCreated -> {
        fetchMediaDetails()
      }
    }
  }

  private fun fetchMediaDetails() {
    viewModelScope.launch {
      val mediaId = savedStateHandle.get<String>("media_details_id")?.toIntOrNull() ?: 0
      getMediaDetailsUseCase(mediaId)
        .collect { data ->
          uiStateFactory.create(data).let { state ->
            _uiState.emit(state)
          }
        }
    }
  }
}

internal class MediaDetailsUiStateFactory @Inject constructor() {

  fun create(response: DomainResult<MediaDetails>): MediaDetailsUiState {
    return when (response) {
      is DomainResult.Success -> {
        MediaDetailsUiState.Success(response.data)
      }

      is DomainResult.Error -> {
        MediaDetailsUiState.Error
      }
    }
  }
}

sealed interface MediaDetailsIntent {

  data object OnScreenCreated : MediaDetailsIntent
}

sealed interface MediaDetailsUiState {
  data object Loading : MediaDetailsUiState
  data class Success(val mediaDetails: MediaDetails) : MediaDetailsUiState
  data object Error : MediaDetailsUiState
}
