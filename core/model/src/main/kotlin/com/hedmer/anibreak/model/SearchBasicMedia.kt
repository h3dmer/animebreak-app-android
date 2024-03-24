package com.hedmer.anibreak.model

data class SearchBasicMedia(
  val id: Int,
  val type: AnibreakMediaType?,
  val title: MediaTitle?,
  val coverImage: String?,
  val season: MediaSeason,
  val rating: Int,
)
