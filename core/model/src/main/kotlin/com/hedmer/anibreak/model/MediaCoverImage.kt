package com.hedmer.anibreak.model

import kotlinx.serialization.Serializable

@Serializable
data class MediaCoverImage(
  val extraLarge: String?,
  val large: String?,
  val medium: String?,
  val color: String?
)
