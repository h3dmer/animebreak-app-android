package com.hedmer.anibreak.search

sealed interface SearchUiState {

  data object Loading : SearchUiState

  data object EmptyQuery : SearchUiState

  data object LoadFailed : SearchUiState

  data class Success(
    val totalListCount: Int? = null
  ) : SearchUiState
}
