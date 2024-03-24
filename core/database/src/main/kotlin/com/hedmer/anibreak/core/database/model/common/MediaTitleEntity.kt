package com.hedmer.anibreak.core.database.model.common

import androidx.room.ColumnInfo


data class MediaTitleEntity(
  @ColumnInfo(name = "title_romaji") val romaji: String?,
  @ColumnInfo(name = "title_english") val english: String?,
  @ColumnInfo(name = "title_native") val native: String?
)