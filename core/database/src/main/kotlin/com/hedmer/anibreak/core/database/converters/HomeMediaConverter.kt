package com.hedmer.anibreak.core.database.converters

import androidx.room.TypeConverter
import com.hedmer.anibreak.model.HomeMedia
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class HomeMediaConverter {

  private val json = Json { ignoreUnknownKeys = true }

  @TypeConverter
  fun homeMediaToJson(value: HomeMedia?): String {
    return json.encodeToString(value)
  }

  @TypeConverter
  fun jsonToHomeMedia(value: String): HomeMedia {
    return json.decodeFromString(value)
  }
}