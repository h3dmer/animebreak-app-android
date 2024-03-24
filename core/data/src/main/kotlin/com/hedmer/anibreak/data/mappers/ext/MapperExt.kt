package com.hedmer.anibreak.data.mappers.ext

import com.hedmer.anibreak.core.network.client.DataState
import com.hedmer.anibreak.domain.mapper.DomainMapper
import com.hedmer.anibreak.domain.mapper.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R> Flow<DataState<T>>.mapToDomain(
  mapper: DomainMapper<T, R>
): Flow<DomainResult<R>> {
  return this.map { dataState ->
    when (dataState) {
      is DataState.Success -> {
        DomainResult.Success(mapper.mapToDomain(dataState.data))
      }

      is DataState.Error -> {
        DomainResult.Error(dataState.exception)
      }
    }
  }
}
