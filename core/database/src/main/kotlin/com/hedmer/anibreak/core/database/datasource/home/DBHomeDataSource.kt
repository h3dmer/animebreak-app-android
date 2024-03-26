package com.hedmer.anibreak.core.database.datasource.home

import com.hedmer.anibreak.core.database.dao.HomeMediaDao
import com.hedmer.anibreak.core.database.model.HomeMediaEntity
import com.hedmer.anibreak.model.HomeMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DatabaseHomeDataSource @Inject constructor(
  private val dao: HomeMediaDao
): DBHomeDataSource {

  override suspend fun upsertHomeMedia(homeMedia: HomeMedia) {
    dao.upsertHomeMedia(
      HomeMediaEntity(
        homeMedia = homeMedia
      )
    )
  }

  override fun getHomeMedia(): Flow<HomeMedia?> {
    return dao.getHomeMedia()
      .map { it?.homeMedia }
  }
}

interface DBHomeDataSource {

  suspend fun upsertHomeMedia(homeMedia: HomeMedia)
  fun getHomeMedia(): Flow<HomeMedia?>
}
