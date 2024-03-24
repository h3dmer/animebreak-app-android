package com.hedmer.anibreak.data.error

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.hedmer.anibreak.common.error.MissingDataException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <D : Operation.Data> Flow<ApolloResponse<D>>.asResult(
  anibreakErrorHandler: AnibreakErrorHandler
): Flow<Result<D>> {
  return anibreakErrorHandler.run {
    handleResponse().handleError()
  }
}

private fun <D : Operation.Data> Flow<ApolloResponse<D>>.handleResponse(): Flow<Result<D>> {
  return map { apolloResponse ->
    if (!apolloResponse.hasErrors()) {
      apolloResponse.data?.let { data ->
        Result.success(data)
      } ?: Result.failure(MissingDataException())
    } else {
      Result.failure(Exception(apolloResponse.errors?.firstOrNull()?.message ?: ""))
    }
  }
}
