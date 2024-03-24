package com.hedmer.anibreak.core.database.model.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
  tableName = "recentSearchQueries",
)
data class RecentSearchQueryEntity(
  @PrimaryKey
  val query: String,
  @ColumnInfo
  val queriedDate: Instant
)
