package com.hedmer.anibreak.data.repository

import com.hedmer.anibreak.common.network.AnibreakDispatchers
import com.hedmer.anibreak.common.network.Dispatcher
import com.hedmer.anibreak.core.database.datasource.home.DBHomeDataSource
import com.hedmer.anibreak.core.network.client.AnibreakGraphqlClient
import com.hedmer.anibreak.data.mappers.HomeAnimeQueryMapper
import com.hedmer.anibreak.data.mappers.MediaDetailsMapper
import com.hedmer.anibreak.data.mappers.ext.mapToDomain
import com.hedmer.anibreak.data.util.MediaSeasonFactory
import com.hedmer.anibreak.domain.mapper.DomainResult
import com.hedmer.anibreak.domain.repository.HomeRepository
import com.hedmer.anibreak.common.util.Result
import com.hedmer.anibreak.domain.repository.runCatchingResult
import com.hedmer.anibreak.model.HomeMedia
import com.hedmer.anibreak.model.MediaDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

internal class DefaultHomeRepository @Inject constructor(
  private val aniBreakClient: AnibreakGraphqlClient,
  private val homeAnimeQueryMapper: HomeAnimeQueryMapper,
  @Dispatcher(AnibreakDispatchers.IO)
  private val ioDispatcher: CoroutineDispatcher,
  private val mediaDetailsMapper: MediaDetailsMapper,
  private val dbHomeDataSource: DBHomeDataSource
) : HomeRepository {

  override fun observeHomeMedia(): Flow<Result<HomeMedia>> {
    return dbHomeDataSource.getHomeMedia()
      .flatMapLatest(::onEmptyHomeData)
  }

  override fun getMedialDetails(id: Int?): Flow<DomainResult<MediaDetails>> {
    return aniBreakClient.getMediaDetails(id)
      .mapToDomain(mediaDetailsMapper)
      .flowOn(ioDispatcher)
  }

  override suspend fun refreshHomeMediaFromNetwork(): Result<HomeMedia> {
    return runCatchingResult {
      withContext(ioDispatcher) {
        fetchRemoteHomeMedia()
          .let { homeMedia ->
            dbHomeDataSource.upsertHomeMedia(homeMedia)
            homeMedia
          }
      }
    }
  }

  private fun onEmptyHomeData(homeMedia: HomeMedia?): Flow<Result<HomeMedia>> {
    return if (homeMedia == null) {
      flow {
        emit(refreshHomeMediaFromNetwork())
      }
    } else {
      flowOf(Result.Success(homeMedia))
    }
  }

  private suspend fun fetchRemoteHomeMedia(): HomeMedia {
    val currentSeason = MediaSeasonFactory.createCurrentSeasonDate(LocalDate.now())
    val nextSeason = MediaSeasonFactory.createNextSeasonDate(LocalDate.now())

    return aniBreakClient.getHomeData(
      currentSeason.season.name,
      currentSeason.year,
      nextSeason.season.name,
      nextSeason.year
    )
      .dataAssertNoErrors
      .let(homeAnimeQueryMapper::mapToDomain)
  }
}
