package com.hedmer.anibreak.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.domain.repository.SearchRepository
import com.hedmer.anibreak.search.filter.SearchFilters
import com.hedmer.anibreak.model.search.RecentLocalSearches
import com.hedmer.anibreak.model.search.SearchResult
import com.hedmer.anibreak.model.search.toParams
import com.hedmer.anibreak.search.data.FiltersHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  filterHolder: FiltersHolder,
  private val savedStateHandle: SavedStateHandle,
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val _uiState: MutableStateFlow<SearchUiState> =
    MutableStateFlow(SearchUiState.EmptyQuery)
  val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

  val searchQuery: StateFlow<String> = fromStateHandle()

  val totalItemsCount: StateFlow<Int?> =
    searchRepository.totalCountsFlow.stateIn()

  val filters: StateFlow<SearchFilters> =
    filterHolder.filters
      .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SearchFilters.Empty
      )

  val recentSearchers: StateFlow<RecentLocalSearches?> =
    searchRepository
      .fetchLocalRecentSearches()
      .stateIn()

  @OptIn(FlowPreview::class)
  val searchResults: Flow<PagingData<SearchBasicMedia>> =
    combine(searchQuery, filters) { query, search ->
      SearchResult(query, search.filters)
    }
      .filter { search -> search.query.trim().isNotEmpty() }
      .debounce(700L)
      .distinctUntilChanged()
      .flatMapLatest { searchParam ->
        _uiState.value = SearchUiState.Loading
        searchRepository.searchMedia(searchParam.toParams())
          .mapLatest { mediaResults ->
            _uiState.value = SearchUiState.Success()
            mediaResults
          }
          .cachedIn(viewModelScope)
      }

  fun onSearchQueryChange(query: String) {
    savedStateHandle[SEARCH_QUERY_STATE_KEY] = query
  }

  fun onSearchTriggered(query: String) {
    if (query.trim().isNotEmpty()) {
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
  }
}
