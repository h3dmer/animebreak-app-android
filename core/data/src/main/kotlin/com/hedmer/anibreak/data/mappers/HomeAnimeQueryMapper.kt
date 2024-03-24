package com.hedmer.anibreak.data.mappers

import com.hedmer.anibreak.domain.mapper.DomainMapper
import com.hedmer.anibreak.model.BasicMediaDetails
import com.hedmer.anibreak.model.HomeMedia
import fragment.BasicMediaDetailsFragment
import query.HomeAnimeQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeAnimeQueryMapper @Inject constructor(
  private val mediaCoverImageMapper: MediaCoverImageMapper
) : DomainMapper<HomeAnimeQuery.Data, HomeMedia> {

  override fun mapToDomain(data: HomeAnimeQuery.Data): HomeMedia {
    return HomeMedia(
      trendingNow = data.trendingNow?.toDomain() ?: emptyList(),
      popularThisSeason = data.popularThisSeason?.toDomain() ?: emptyList(),
      upcomingNextSeason = data.upcomingNextSeason?.toDomain() ?: emptyList(),
      allTimePopular = data.allTimePopular?.toDomain() ?: emptyList(),
      topTen = data.top10?.toDomain() ?: emptyList()
    )
  }

  private fun BasicMediaDetailsFragment.toDomain(): BasicMediaDetails {
    return BasicMediaDetails(
      id = id,
      title = title?.english ?: (title?.native ?: ""),
      coverImage = mediaCoverImageMapper.mapToDomain(fragments.mediaCoverImageFragment),
      bannerImage = bannerImage,
    )
  }

  private fun HomeAnimeQuery.TrendingNow.toDomain(): List<BasicMediaDetails> {
    return media?.filterNotNull()?.map {
      it.fragments.basicMediaDetailsFragment.toDomain()
    } ?: emptyList()
  }

  private fun HomeAnimeQuery.PopularThisSeason.toDomain(): List<BasicMediaDetails> {
    return media?.filterNotNull()?.map {
      it.fragments.basicMediaDetailsFragment.toDomain()
    } ?: emptyList()
  }

  private fun HomeAnimeQuery.AllTimePopular.toDomain(): List<BasicMediaDetails> {
    return media?.filterNotNull()?.map {
      it.fragments.basicMediaDetailsFragment.toDomain()
    } ?: emptyList()
  }

  private fun HomeAnimeQuery.UpcomingNextSeason.toDomain(): List<BasicMediaDetails> {
    return media?.filterNotNull()?.map {
      it.fragments.basicMediaDetailsFragment.toDomain()
    } ?: emptyList()
  }

  private fun HomeAnimeQuery.Top10.toDomain(): List<BasicMediaDetails> {
    return media?.filterNotNull()?.map {
      it.fragments.basicMediaDetailsFragment.toDomain()
    } ?: emptyList()
  }
}
