package com.hedmer.anibreak.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hedmer.anibreak.core.database.converters.HomeMediaConverter
import com.hedmer.anibreak.core.database.converters.InstantConverter
import com.hedmer.anibreak.core.database.converters.MediaSeasonConverter
import com.hedmer.anibreak.core.database.dao.HomeMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchQueryDao
import com.hedmer.anibreak.core.database.model.HomeMediaEntity
import com.hedmer.anibreak.core.database.model.search.RecentSearchMediaEntity
import com.hedmer.anibreak.core.database.model.search.RecentSearchQueryEntity

@Database(
  exportSchema = false,
  version = 2,
  entities = [
    HomeMediaEntity::class,
    RecentSearchMediaEntity::class,
    RecentSearchQueryEntity::class
  ]
)
@TypeConverters(
  HomeMediaConverter::class,
  InstantConverter::class,
  MediaSeasonConverter::class
)
internal abstract class AnibreakDatabase : RoomDatabase() {

  abstract fun homeMediaDao(): HomeMediaDao

  abstract fun recentSearchQueryDao(): RecentSearchQueryDao

  abstract fun recentSearchMediaDao(): RecentSearchMediaDao

}
