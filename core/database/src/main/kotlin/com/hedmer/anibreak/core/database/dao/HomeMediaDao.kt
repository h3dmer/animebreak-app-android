package com.hedmer.anibreak.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hedmer.anibreak.core.database.model.HomeMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface HomeMediaDao {

  @Upsert
  suspend fun upsertHomeMedia(homeMediaEntity: HomeMediaEntity)

  @Query("SELECT * FROM homeMediaEntity WHERE id = :id")
  fun getHomeMedia(
    id: Int = HomeMediaEntity.FIRST_RECORD_ID
  ): Flow<HomeMediaEntity?>
}
