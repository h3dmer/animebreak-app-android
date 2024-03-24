package com.hedmer.anibreak.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hedmer.anibreak.core.database.model.search.RecentSearchQueryEntity
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_LIMIT = 5

@Dao
interface RecentSearchQueryDao {

  @Query(value = "SELECT * FROM recentSearchQueries ORDER BY 'query' DESC LIMIT :limit")
  fun getRecentSearchQueryEntities(
    limit: Int = DEFAULT_LIMIT
  ): Flow<List<RecentSearchQueryEntity>>

  @Upsert
  suspend fun upsertRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

  @Query(value = "DELETE FROM recentSearchQueries")
  suspend fun clearRecentSearchQueries()

  @Query(value = "DELETE FROM recentSearchQueries WHERE `query` = :query")
  suspend fun removeRecentSearchQuery(query: String)
}
