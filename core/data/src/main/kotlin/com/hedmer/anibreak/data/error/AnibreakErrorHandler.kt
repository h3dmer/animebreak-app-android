package com.hedmer.anibreak.data.error

import kotlinx.coroutines.flow.Flow

interface AnibreakErrorHandler {

  fun <T> Flow<Result<T>>.handleError(): Flow<Result<T>>
}
