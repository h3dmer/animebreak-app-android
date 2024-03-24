package com.hedmer.anibreak.core.database.model.search

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hedmer.anibreak.core.database.model.common.MediaTitleEntity
import com.hedmer.anibreak.model.MediaSeason
import java.time.Instant

@Entity(
  tableName = "recentSearchMedia"
)
data class RecentSearchMediaEntity(
  @PrimaryKey
  val id: Int,
  @Embedded(prefix = "title_") val title: MediaTitleEntity?,
  @ColumnInfo
  val coverImage: String?,
  val season: MediaSeason
)
