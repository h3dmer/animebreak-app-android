package com.hedmer.anibreak.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.domain.repository.SearchRepository
import com.hedmer.anibreak.model.search.RecentLocalSearches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val savedStateHandle: SavedStateHandle,
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val searchQuery: StateFlow<String> = fromStateHandle()

  private val _uiState: MutableStateFlow<SearchUiState> =
    MutableStateFlow(SearchUiState.EmptyQuery)
  val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

  val totalItemsCount: StateFlow<Int?> =
    searchRepository.totalCountsFlow.stateIn()

  val recentSearchers: StateFlow<RecentLocalSearches?> =
    searchRepository
      .fetchLocalRecentSearches()
      .stateIn()

  val searchResults: Flow<PagingData<SearchBasicMedia>> =
    searchQuery
      .filter(::atLeastOneLetter)
      .distinctUntilChanged()
      .flatMapLatest { query ->
        _uiState.value = SearchUiState.Loading
        searchRepository.searchMedia(1, query)
          .mapLatest { mediaResults ->
            _uiState.value = SearchUiState.Success()
            mediaResults
          }
          .cachedIn(viewModelScope)
      }

  fun onSearchTriggered(query: String) {
    if (atLeastOneLetter(query)) {
      viewModelScope.launch {
        searchRepository.upsertRecentSearchQuery(query)
      }
      savedStateHandle[SEARCH_QUERY_STATE_KEY] = query
    }
  }

  fun onRemoveSearch(query: String) {
    viewModelScope.launch {
      searchRepository.removeRecentQuery(query)
    }
  }

  fun onMediaSearchClick(searchBasicMedia: SearchBasicMedia) {
    viewModelScope.launch {
      searchRepository.upsertRecentSearchMedia(searchBasicMedia)
    }
  }

  fun onRemoveRecentMediaList() {
    viewModelScope.launch {
      searchRepository.removeAllRecentMedia()
    }
  }

  fun onClearContent() {
    _uiState.value = SearchUiState.EmptyQuery
  }

  private fun atLeastOneLetter(query: String): Boolean {
    return query.trim().length >= SEARCH_QUERY_MIN_LENGTH
  }

  private fun fromStateHandle(): StateFlow<String> {
    return savedStateHandle.getStateFlow(
      key = SEARCH_QUERY_STATE_KEY, initialValue = ""
    )
  }

  private fun <T> Flow<T>.stateIn(initValue: T? = null): StateFlow<T?> {
    return this.stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
      initialValue = initValue
    )
  }

  private companion object {
    const val SEARCH_QUERY_STATE_KEY = "searchQuery"
    const val SEARCH_QUERY_MIN_LENGTH = 1
  }
}
