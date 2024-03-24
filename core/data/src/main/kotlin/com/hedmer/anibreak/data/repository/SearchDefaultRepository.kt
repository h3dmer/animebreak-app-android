package com.hedmer.anibreak.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hedmer.anibreak.core.database.dao.RecentSearchMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchQueryDao
import com.hedmer.anibreak.core.database.model.search.RecentSearchQueryEntity
import com.hedmer.anibreak.core.network.client.AnibreakGraphqlClient
import com.hedmer.anibreak.data.model.mapToDomain
import com.hedmer.anibreak.data.model.toDomain
import com.hedmer.anibreak.data.model.toEntity
import com.hedmer.anibreak.data.paging.SearchMediaPagingSource
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.domain.repository.SearchRepository
import com.hedmer.anibreak.model.search.RecentLocalSearches
import com.hedmer.anibreak.model.search.RecentSearchQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDefaultRepository @Inject constructor(
  private val apiClient: AnibreakGraphqlClient,
  private val searchQueryDao: RecentSearchQueryDao,
  private val searchMediaDao: RecentSearchMediaDao
) : SearchRepository {

  private val _totalCountsFlow = MutableStateFlow<Int?>(null)
  override val totalCountsFlow: StateFlow<Int?> = _totalCountsFlow.asStateFlow()

  override fun searchMedia(
    page: Int,
    query: String
  ): Flow<PagingData<SearchBasicMedia>> {

    val pagingSource = SearchMediaPagingSource(apiClient, query, PAGE_SIZE) {
       _totalCountsFlow.value = it
    }
    val config = PagingConfig(PAGE_SIZE)
    return Pager(
      config = config,
      pagingSourceFactory = { pagingSource }
    )
      .flow
      .mapToDomain()
  }

  override suspend fun upsertRecentSearchQuery(query: String) =
    searchQueryDao.upsertRecentSearchQuery(
      RecentSearchQueryEntity(
        query,
        Clock.System.now()
      )
    )

  override suspend fun upsertRecentSearchMedia(media: SearchBasicMedia) {
    searchMediaDao.upsertRecentSearchMedia(
      media.toEntity()
    )
  }

  override suspend fun removeRecentQuery(query: String) {
    searchQueryDao.removeRecentSearchQuery(query)
  }

  override fun fetchLocalRecentSearches(): Flow<RecentLocalSearches?> {
    return combine(
      fetchRecentQueries(),
      fetchRecentSearchMedia()
    ) { queries, media ->
      RecentLocalSearches(queries, media)
    }
  }

  override suspend fun removeAllRecentMedia() {
    searchMediaDao.clearRecentSearchMediaEntities()
  }

  private fun fetchRecentQueries() =
    searchQueryDao.getRecentSearchQueryEntities()
      .map { entities ->
        entities.map { entity ->
          RecentSearchQuery(
            entity.query,
            entity.queriedDate.toString()
          )
        }
      }

  private fun fetchRecentSearchMedia() =
    searchMediaDao.getRecentSearchMediaEntities()
      .map { entities ->
        entities?.map { entity -> entity.toDomain() }
      }

  private companion object {
    const val PAGE_SIZE = 20
  }
}
