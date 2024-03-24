package com.hedmer.anibreak.model

import kotlinx.serialization.Serializable

@Serializable
data class BasicMediaDetails(
  val id: Int,
  val title: String,
  val coverImage: MediaCoverImage?,
  val bannerImage: String?
)
