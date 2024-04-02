package com.hedmer.anibreak.search.data

import com.hedmer.anibreak.common.extension.BiConsumer
import com.hedmer.anibreak.common.extension.Consumer
import com.hedmer.anibreak.model.AnibreakMediaType
import com.hedmer.anibreak.model.MediaFormat
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.search.filter.SearchFilterResource
import com.hedmer.anibreak.search.filter.SearchFilters
import com.hedmer.anibreak.model.search.FilterDataType
import com.hedmer.anibreak.model.search.FilterState
import java.time.LocalDate
import javax.inject.Inject

class FiltersListFactory @Inject constructor(
  private val filterRes: SearchFilterResource
) {

  fun create(
    onFilterChange: BiConsumer<FilterState, String>,
    onClearFilter: Consumer<FilterState>
  ): SearchFilters {
    return SearchFilters(
      listOf(
        createMediaType(onFilterChange, onClearFilter),
        createMediaSeason(onFilterChange, onClearFilter),
        createMediaFormat(onFilterChange, onClearFilter),
        createMediaYear(onFilterChange, onClearFilter)
      )
    )
  }

  private fun createMediaType(
    onFilterChange: BiConsumer<FilterState, String>,
    onClearFilter: Consumer<FilterState>
  ) = FilterState.MediaTypeFilter(
    filterHint = filterRes.mediaTypeHint(),
    dataTypes = AnibreakMediaType.entries.map { FilterDataType(it.name) },
    onFilterChange = onFilterChange,
    onClearFilter = onClearFilter
  )

  private fun createMediaSeason(
    onFilterChange: BiConsumer<FilterState, String>,
    onClearFilter: Consumer<FilterState>
  ) = FilterState.MediaSeasonFilter(
      filterHint = filterRes.mediaSeasonHint(),
      dataTypes = MediaSeason.entries.map { FilterDataType(it.name) },
      onFilterChange = onFilterChange,
    onClearFilter = onClearFilter
    )

  private fun createMediaFormat(
    onFilterChange: BiConsumer<FilterState, String>,
    onClearFilter: Consumer<FilterState>
  ) = FilterState.MediaSeasonFilter(
    filterHint = filterRes.mediaFormatHint(),
    dataTypes = MediaFormat.entries.map { FilterDataType(it.name) },
    onFilterChange = onFilterChange,
    onClearFilter = onClearFilter
  )

  private fun createMediaYear(
    onFilterChange: BiConsumer<FilterState, String>,
    onClearFilter: Consumer<FilterState>
  ) = FilterState.MediaSeasonFilter(
    filterHint = filterRes.mediaYearHint(),
    dataTypes = (LocalDate.now().year + 1 downTo 1960)
      .map { FilterDataType(it.toString()) },
    onFilterChange = onFilterChange,
    onClearFilter = onClearFilter
  )
}
