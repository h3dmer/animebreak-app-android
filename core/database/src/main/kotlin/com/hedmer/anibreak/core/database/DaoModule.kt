package com.hedmer.anibreak.core.database

import com.hedmer.anibreak.core.database.dao.HomeMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

  @Provides
  fun providesRecentSearchQueryDao(
    database: AnibreakDatabase,
  ): RecentSearchQueryDao = database.recentSearchQueryDao()

  @Provides
  fun providesHomeMeiaDao(
    database: AnibreakDatabase,
  ): HomeMediaDao = database.homeMediaDao()

  @Provides
  fun providesRecentSearchMediaDao(
    database: AnibreakDatabase,
  ): RecentSearchMediaDao = database.recentSearchMediaDao()

}