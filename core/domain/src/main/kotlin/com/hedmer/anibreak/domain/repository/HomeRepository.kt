package com.hedmer.anibreak.domain.repository

import com.hedmer.anibreak.common.util.Result
import com.hedmer.anibreak.domain.mapper.DomainResult
import com.hedmer.anibreak.model.HomeMedia
import com.hedmer.anibreak.model.MediaDetails
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

  fun observeHomeMedia(): Flow<Result<HomeMedia>>

  fun getMedialDetails(id: Int?): Flow<DomainResult<MediaDetails>>

  suspend fun refreshHomeMediaFromNetwork(): Result<HomeMedia>
}

suspend fun <T> runCatchingResult(block: suspend () -> T): Result<T> {
  return try {
    Result.Success(block())
  } catch (e: Exception) {
    Result.Error(e)
  }
}
