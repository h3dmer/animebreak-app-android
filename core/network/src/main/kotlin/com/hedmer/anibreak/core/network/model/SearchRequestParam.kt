package com.hedmer.anibreak.core.network.model

import type.MediaFormat
import type.MediaSeason
import type.MediaType

data class SearchRequestParam(
  val page: Int,
  val query: String,
  val type: MediaType?,
  val season: MediaSeason?,
  val format: MediaFormat?,
  val year: Int?
)
