package com.hedmer.anibreak.model


data class MediaDetails(
  val id: Int,
  val title: MediaTitle?,
  val description: String,
  val bannerImage: String?,
  val coverImage: MediaCoverImage?,
  val type: AnibreakMediaType?,
  val date: MediaDateDetails,
  val mediaTrailer: MediaTrailer?,
  val rating: MediaRating,
  val genres: List<String>,
  val mediaCharacters: List<MediaParticipant>?,
  val mediaStaff: List<MediaParticipant>?
)

data class MediaTitle(
  val romaji: String?,
  val english: String?,
  val native: String?
) {

  fun getAnyNameOrEmpty(): String {
    return romaji ?: native ?: english  ?: ""
  }
}

data class MediaDateDetails(
  val startDate: MediaDate,
  val endDate: MediaDate
)

data class MediaContent(
  val episodes: Int,
  val duration: Int,
  val chapters: Int,
  val volumes: Int
)

enum class AnibreakMediaType {
  ANIME, MANGA
}

data class MediaTrailer(
  val id: String?,
  val site: String?,
  val thumbnail: String?
)

data class MediaRating(
  val averageScore: Int?,
  val meanScore: Int?
)

sealed interface MediaParticipant {
  val id: Int
  val name: String
  val imageUrl: String
}

data class MediaCharacter(
  override val id: Int,
  override val name: String,
  override val imageUrl: String
): MediaParticipant

data class MediaStaff(
  override val id: Int,
  override val name: String,
  override val imageUrl: String
): MediaParticipant





