package com.hedmer.anibreak.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hedmer.anibreak.core.database.model.search.RecentSearchMediaEntity
import kotlinx.coroutines.flow.Flow

private const val RECENT_MEDIA_LIMIT = 5

@Dao
interface RecentSearchMediaDao {

  @Query(value = "SELECT * FROM recentSearchMedia LIMIT :limit")
  fun getRecentSearchMediaEntities(
    limit: Int = RECENT_MEDIA_LIMIT
  ): Flow<List<RecentSearchMediaEntity>?>

  @Upsert
  suspend fun upsertRecentSearchMedia(
    recentSearchMedia: RecentSearchMediaEntity
  )

  @Query(value = "DELETE FROM recentSearchMedia")
  suspend fun clearRecentSearchMediaEntities()
}
