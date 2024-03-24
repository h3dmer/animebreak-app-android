package com.hedmer.anibreak.model.search

import com.hedmer.anibreak.model.SearchBasicMedia

data class RecentLocalSearches(
  val queries: List<RecentSearchQuery>?,
  val media: List<SearchBasicMedia>?
) {

  fun anyAvailable() = !queries.isNullOrEmpty() || !media.isNullOrEmpty()
}
