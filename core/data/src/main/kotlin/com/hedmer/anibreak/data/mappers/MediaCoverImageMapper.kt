package com.hedmer.anibreak.data.mappers

import com.hedmer.anibreak.domain.mapper.DomainMapper
import com.hedmer.anibreak.model.MediaCoverImage
import fragment.MediaCoverImageFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MediaCoverImageMapper @Inject constructor() :
  DomainMapper<MediaCoverImageFragment, MediaCoverImage> {

  override fun mapToDomain(data: MediaCoverImageFragment): MediaCoverImage {
    return MediaCoverImage(
      extraLarge = data.coverImage?.extraLarge,
      large = data.coverImage?.large,
      medium = data.coverImage?.medium,
      color = null
    )
  }
}
