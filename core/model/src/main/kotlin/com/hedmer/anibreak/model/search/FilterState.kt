package com.hedmer.anibreak.model.search

sealed interface FilterState {

  val filterHint: String
  val chosenFilterName: String?
  val dataTypes: List<FilterDataType>
  val onFilterChange: (FilterState, String) -> Unit
  val onClearFilter: (FilterState) -> Unit

  data class MediaTypeFilter(
    override val filterHint: String,
    override val chosenFilterName: String? = null,
    override val dataTypes: List<FilterDataType>,
    override val onFilterChange: (FilterState, String) -> Unit,
    override val onClearFilter: (FilterState) -> Unit
  ): FilterState

  data class MediaSeasonFilter(
    override val filterHint: String,
    override val chosenFilterName: String? = null,
    override val dataTypes: List<FilterDataType>,
    override val onFilterChange: (FilterState, String) -> Unit,
    override val onClearFilter: (FilterState) -> Unit
  ): FilterState

  data class MediaFormatFilter(
    override val filterHint: String,
    override val chosenFilterName: String? = null,
    override val dataTypes: List<FilterDataType>,
    override val onFilterChange: (FilterState, String) -> Unit,
    override val onClearFilter: (FilterState) -> Unit
  ): FilterState

  data class MediaSeasonYearFilter(
    override val filterHint: String,
    override val chosenFilterName: String? = null,
    override val dataTypes: List<FilterDataType>,
    override val onFilterChange: (FilterState, String) -> Unit,
    override val onClearFilter: (FilterState) -> Unit
  ): FilterState
}
