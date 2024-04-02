package com.hedmer.anibreak.domain.repository

import androidx.paging.PagingData
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.model.search.RecentLocalSearches
import com.hedmer.anibreak.model.search.SearchParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchRepository {

  val totalCountsFlow: StateFlow<Int?>

  fun searchMedia(searchParam: SearchParam): Flow<PagingData<SearchBasicMedia>>

  fun fetchLocalRecentSearches(): Flow<RecentLocalSearches?>

  suspend fun upsertRecentSearchQuery(query: String)

  suspend fun upsertRecentSearchMedia(media: SearchBasicMedia)

  suspend fun removeRecentQuery(query: String)

  suspend fun removeAllRecentMedia()
}
