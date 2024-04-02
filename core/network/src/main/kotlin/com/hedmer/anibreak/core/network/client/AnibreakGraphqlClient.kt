package com.hedmer.anibreak.core.network.client

import MediaQuery
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.hedmer.anibreak.common.error.MissingDataException
import com.hedmer.anibreak.common.error.ResponseError
import com.hedmer.anibreak.core.network.model.SearchRequestParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import query.HomeAnimeQuery
import query.SearchMediaQuery
import type.MediaSeason
import javax.inject.Inject

class AnibreakGraphqlClient @Inject constructor(
  private val client: ApolloClient
) {

  suspend fun getHomeData(
    currentSeason: String,
    currentYear: Int,
    nextSeason: String,
    nextSeasonYear: Int
  ): ApolloResponse<HomeAnimeQuery.Data> {
    return client.query(
      HomeAnimeQuery(
        currentSeason = Optional.present(MediaSeason.safeValueOf(currentSeason)),
        currentSeasonYear = Optional.present(currentYear),
        upcomingSeason = Optional.present(MediaSeason.safeValueOf(nextSeason)),
        upcomingSeasonYear = Optional.present(nextSeasonYear)
      )
    ).execute()
  }

  fun getMediaDetails(id: Int?): Flow<DataState<MediaQuery.Data>> {
    return client.query(
      MediaQuery(
        id = Optional.presentIfNotNull(id)
      )
    )
      .toFlow()
      .toDataState()
  }

  suspend fun searchMedia(
    searchParam: SearchRequestParam
  ): ApolloResponse<SearchMediaQuery.Data> {
    return with(searchParam) {
      client.query(
        SearchMediaQuery(
          page = page,
          search = query,
          mediaType = Optional.presentIfNotNull(type),
          season = Optional.presentIfNotNull(season),
          format = Optional.presentIfNotNull(format),
          seasonYear = Optional.presentIfNotNull(year),
        )
      ).execute()
    }
  }
}

sealed interface DataState<out T> {
  data class Success<T>(val data: T) : DataState<T>
  data class Error(val exception: Throwable) : DataState<Nothing>
}

fun <D : Operation.Data> Flow<ApolloResponse<D>>.toDataState(): Flow<DataState<D>> {
  return map { apolloResponse ->
    if (apolloResponse.hasErrors()) {
      DataState.Error(
        ResponseError(apolloResponse.errors?.firstOrNull()?.message ?: "")
      )
    } else {
      apolloResponse.data?.let { data ->
        DataState.Success(data)
      } ?: DataState.Error(MissingDataException())
    }
  }.catch { throwable ->
    emit(DataState.Error(throwable))
  }
}
