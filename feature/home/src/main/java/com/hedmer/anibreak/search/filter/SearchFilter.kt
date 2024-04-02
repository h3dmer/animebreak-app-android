package com.hedmer.anibreak.search.filter

import androidx.compose.runtime.Immutable
import com.hedmer.anibreak.model.search.FilterState

@Immutable
data class SearchFilters(
  val filters: List<FilterState>
) {

  companion object {
    val Empty = SearchFilters(emptyList())
  }
}
