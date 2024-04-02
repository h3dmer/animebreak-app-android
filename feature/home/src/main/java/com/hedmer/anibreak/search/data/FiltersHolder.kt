package com.hedmer.anibreak.search.data

import com.hedmer.anibreak.search.filter.SearchFilters
import com.hedmer.anibreak.model.search.FilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FiltersHolder @Inject constructor(
  dataFactory: FiltersListFactory
) {

  private val _filters = MutableStateFlow(
    dataFactory.create(::onFilterAction, ::onClearFilter)
  )
  val filters: StateFlow<SearchFilters>
    get() = _filters

  private fun onFilterAction(state: FilterState, option: String) {
    updateFilterState(state, option)
  }

  private fun onClearFilter(state: FilterState) {
    updateFilterState(state, null)
  }

  private fun updateFilterState(state: FilterState, option: String?) {
    val updatedFilters = _filters.value.filters.map { filter ->
      if (filter == state) {
        when (state) {
          is FilterState.MediaTypeFilter -> state.copy(chosenFilterName = option)
          is FilterState.MediaSeasonFilter -> state.copy(chosenFilterName = option)
          is FilterState.MediaFormatFilter -> state.copy(chosenFilterName = option)
          is FilterState.MediaSeasonYearFilter -> state.copy(chosenFilterName = option)
        }
      } else {
        filter
      }
    }

    _filters.value = _filters.value.copy(filters = updatedFilters)
  }
}
