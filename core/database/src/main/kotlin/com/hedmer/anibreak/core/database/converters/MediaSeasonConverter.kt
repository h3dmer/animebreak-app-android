package com.hedmer.anibreak.core.database.converters

import androidx.room.TypeConverter
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.model.MediaSeason.valueOf

class MediaSeasonConverter {

  @TypeConverter
  fun fromMediaSeason(season: MediaSeason) = season.name

  @TypeConverter
  fun toMediaSeason(season: String) = valueOf(season)
}
