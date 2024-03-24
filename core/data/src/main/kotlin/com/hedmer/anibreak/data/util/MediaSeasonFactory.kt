package com.hedmer.anibreak.data.util

import com.hedmer.anibreak.model.MediaSeason
import java.time.LocalDate

data class MediaSeasonDate(
  val season: MediaSeason,
  val year: Int
)

object MediaSeasonFactory {

  fun createCurrentSeasonDate(localDate: LocalDate): MediaSeasonDate {
    return MediaSeasonDate(
      getMediaSeasonByMonth(localDate.monthValue),
      localDate.year
    )
  }

  fun createNextSeasonDate(localDate: LocalDate): MediaSeasonDate {
    return when (isFallSeason(localDate)) {
      true -> MediaSeasonDate(MediaSeason.WINTER, localDate.year + 1)
      false -> {
        MediaSeasonDate(
          getMediaSeasonByMonth(localDate.monthValue + 3),
          localDate.year
        )
      }
    }
  }

  private fun getMediaSeasonByMonth(
    monthNumber: Int
  ): MediaSeason {
    return when (monthNumber) {
      in 1..3 -> MediaSeason.WINTER
      in 4..6 -> MediaSeason.SPRING
      in 7..9 -> MediaSeason.SUMMER
      in 10..12 -> MediaSeason.FALL
      else -> throw IllegalArgumentException(
        "Invalid month: $monthNumber"
      )
    }
  }

  private fun isFallSeason(localDate: LocalDate) =
    localDate.monthValue >= 10
}