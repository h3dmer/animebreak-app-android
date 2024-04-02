package com.hedmer.anibreak.model.search

import com.hedmer.anibreak.model.search.FilterState.MediaFormatFilter
import com.hedmer.anibreak.model.search.FilterState.MediaSeasonFilter
import com.hedmer.anibreak.model.search.FilterState.MediaTypeFilter
import com.hedmer.anibreak.model.search.FilterState.MediaSeasonYearFilter

data class SearchResult(
  val query: String,
  val searchFilter: List<FilterState>
)

data class SearchParam(
  val page: Int,
  val query: String,
  val type: String?,
  val season: String?,
  val format: String?,
  val year: String?
)

fun SearchResult.toParams(): SearchParam {
  val filterMap = searchFilter
    .filter { it.chosenFilterName != null }
    .associateBy { filter ->
      when (filter) {
        is MediaTypeFilter -> "type"
        is MediaSeasonFilter -> "season"
        is MediaFormatFilter -> "format"
        is MediaSeasonYearFilter -> "year"
      }
    }

  return SearchParam(
    page = 1,
    query = query,
    type = (filterMap["type"] as? MediaTypeFilter)?.chosenFilterName,
    season = (filterMap["season"] as? MediaSeasonFilter)?.chosenFilterName,
    format = (filterMap["format"] as? MediaFormatFilter)?.chosenFilterName,
    year = (filterMap["year"] as? MediaSeasonYearFilter)?.chosenFilterName,
  )
}
