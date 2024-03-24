package com.hedmer.anibreak.data.mappers

import MediaQuery
import com.hedmer.anibreak.domain.mapper.DomainMapper
import com.hedmer.anibreak.model.MediaCharacter
import com.hedmer.anibreak.model.MediaDate
import com.hedmer.anibreak.model.MediaDateDetails
import com.hedmer.anibreak.model.MediaDetails
import com.hedmer.anibreak.model.MediaParticipant
import com.hedmer.anibreak.model.MediaRating
import com.hedmer.anibreak.model.MediaStaff
import com.hedmer.anibreak.model.MediaTitle
import com.hedmer.anibreak.model.MediaTrailer
import com.hedmer.anibreak.model.AnibreakMediaType
import fragment.MediaDetailsFragment
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MediaDetailsMapper @Inject constructor(
  private val mediaCoverImageMapper: MediaCoverImageMapper
) : DomainMapper<MediaQuery.Data, MediaDetails> {

  override fun mapToDomain(data: MediaQuery.Data): MediaDetails {
    requireNotNull(data.Media?.fragments?.mediaDetailsFragment).run {
      return MediaDetails(
        id = id,
        title = title?.toDomain(),
        description = description ?: "",
        bannerImage = bannerImage,
        coverImage = mediaCoverImageMapper.mapToDomain(
          fragments.mediaCoverImageFragment
        ),
        type = AnibreakMediaType.MANGA,
        date = createMediaDateDetails(this),
        mediaTrailer = mapMediaTrailer(trailer),
        rating = MediaRating(
          averageScore, meanScore
        ),
        genres = genres?.filterNotNull()?.take(4) ?: listOf(),
        mediaCharacters = characterPreview?.let(::mapCharactersPreview),
        mediaStaff = staff?.let(::mapStaffPreview) ?: listOf()
      )
    }
  }

  private fun MediaDetailsFragment.Title.toDomain(): MediaTitle {
    return MediaTitle(romaji, english, native)
  }

  private fun createMediaDateDetails(
    mediaDetailsFragment: MediaDetailsFragment
  ): MediaDateDetails {
    with(mediaDetailsFragment) {
      return MediaDateDetails(
        startDate = mapMediaDate(
          startDate?.day,
          startDate?.month,
          startDate?.year
        ),
        endDate = mapMediaDate(
          endDate?.day,
          endDate?.month,
          endDate?.year
        )
      )
    }
  }

  private fun mapMediaDate(
    day: Int?, month: Int?, year: Int?
  ) = MediaDate(day, month, year)

  private fun mapMediaTrailer(
    trailer: MediaDetailsFragment.Trailer?
  ): MediaTrailer? {
    return trailer?.let {
      MediaTrailer(
        it.id,
        it.site,
        it.thumbnail,
      )
    }
  }

  private fun mapCharactersPreview(
    data: MediaDetailsFragment.CharacterPreview
  ): List<MediaParticipant> {
    return data.nodes?.filterNotNull()?.take(10)?.map { character ->
      MediaCharacter(
        id = character.id,
        name = character.name?.full ?: "",
        imageUrl = character.image?.large!!
      )
    } ?: listOf()
  }

  private fun mapStaffPreview(
    data: MediaDetailsFragment.Staff
  ): List<MediaParticipant> {
    return data.edges?.filterNotNull()?.take(10)?.mapNotNull { staff ->
      staff.node?.let { node ->
        MediaStaff(
          id = node.id,
          name = node.name?.full ?: "",
          imageUrl = node.image?.large!!
        )
      }
    } ?: listOf()
  }
}
