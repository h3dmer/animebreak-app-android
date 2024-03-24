package com.hedmer.anibreak.data.model

import androidx.paging.PagingData
import androidx.paging.map
import com.hedmer.anibreak.core.database.model.common.MediaTitleEntity
import com.hedmer.anibreak.core.database.model.search.RecentSearchMediaEntity
import com.hedmer.anibreak.model.AnibreakMediaType
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.model.MediaTitle
import com.hedmer.anibreak.model.SearchBasicMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import query.SearchMediaQuery
import type.MediaType

fun Flow<PagingData<SearchMediaQuery.Medium>>.mapToDomain():
    Flow<PagingData<SearchBasicMedia>> {
  return map { pagingData ->
    pagingData.map {
      it.toDomain()
    }
  }
}

fun SearchMediaQuery.Medium.toDomain(): SearchBasicMedia {
  return SearchBasicMedia(
    id = id,
    type = if (type == MediaType.MANGA) {
      AnibreakMediaType.MANGA
    } else {
      AnibreakMediaType.MANGA
    },
    title = MediaTitle("", "", ""),
    coverImage = coverImage?.extraLarge,
    season = MediaSeason.FALL,
    rating = 5
  )
}


fun SearchBasicMedia.toEntity(): RecentSearchMediaEntity =
  RecentSearchMediaEntity(
    id = id,
    title = title?.toEntity(),
    coverImage = coverImage,
    season = season
  )

fun MediaTitle.toEntity(): MediaTitleEntity =
  MediaTitleEntity(
    romaji = romaji,
    english = english,
    native = native
  )

fun RecentSearchMediaEntity.toDomain(): SearchBasicMedia =
  SearchBasicMedia(
    id,
    null,
    title?.toDomain(),
    coverImage = coverImage,
    season = season,
    rating = 0
  )

fun MediaTitleEntity.toDomain(): MediaTitle? {
  return MediaTitle(
    romaji = romaji,
    english = english,
    native = native
  )
}
