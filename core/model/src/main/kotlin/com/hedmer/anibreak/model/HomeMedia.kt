package com.hedmer.anibreak.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeMedia(
  val trendingNow: List<BasicMediaDetails>,
  val popularThisSeason: List<BasicMediaDetails>,
  val upcomingNextSeason: List<BasicMediaDetails>,
  val allTimePopular: List<BasicMediaDetails>,
  val topTen: List<BasicMediaDetails>
) {

  companion object {
    val Empty = HomeMedia(
      trendingNow = emptyList(),
      popularThisSeason = emptyList(),
      upcomingNextSeason = emptyList(),
      allTimePopular = emptyList(),
      topTen = emptyList()
    )
  }
}
