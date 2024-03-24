package com.hedmer.anibreak.model.search

import com.hedmer.anibreak.model.SearchBasicMedia

data class SearchMediaResult(
  val pageInfo: MediaPageInfo?,
  val searchMediaList: List<SearchBasicMedia>
)
