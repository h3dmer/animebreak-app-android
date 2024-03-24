package com.hedmer.anibreak.data.error

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.hedmer.anibreak.common.error.InternetConnectivityException
import com.hedmer.anibreak.common.error.UnknownException
import com.hedmer.anibreak.data.util.AnibreakInternetConnectivityChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandardGraphqlErrorHandler @Inject constructor(
  private val anibreakInternetConnectivityChecker: AnibreakInternetConnectivityChecker
): AnibreakErrorHandler {

  override fun <T> Flow<Result<T>>.handleError(): Flow<Result<T>> {
    return catch {throwable ->
      when {
        isInternetConnectivityProblem(throwable) -> {
          emit(Result.failure(InternetConnectivityException()))
        }
        throwable is ApolloNetworkException -> {
          emit(Result.failure(UnknownException()))
        }
        else -> emit(Result.failure(UnknownException()))
      }
    }
  }

  private fun isInternetConnectivityProblem(throwable: Throwable): Boolean {
    return throwable is ApolloNetworkException && !anibreakInternetConnectivityChecker.hasInternet()
  }
}
