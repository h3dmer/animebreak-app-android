package com.hedmer.anibreak.core.network.interceptor

import com.apollographql.apollo3.api.ApolloRequest
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.interceptor.ApolloInterceptor
import com.apollographql.apollo3.interceptor.ApolloInterceptorChain
import com.hedmer.anibreak.common.error.InternetConnectivityException
import com.hedmer.anibreak.common.error.UnknownException
import com.hedmer.anibreak.common.network.AnibreakInternetConnectivityChecker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorInterceptor @Inject constructor(
  private val connectivityChecker: AnibreakInternetConnectivityChecker
) : ApolloInterceptor {

  override fun <D : Operation.Data> intercept(
    request: ApolloRequest<D>,
    chain: ApolloInterceptorChain
  ): Flow<ApolloResponse<D>> {
    return chain
      .proceed(request)
      .catch {
        handleError(it)
      }
  }

  private fun handleError(throwable: Throwable) {
    throw when {
      isInternetConnectivityProblem(throwable) -> {
        InternetConnectivityException()
      }

      throwable is ApolloNetworkException -> {
        UnknownException()
      }

      else -> UnknownException()
    }
  }

  private fun isInternetConnectivityProblem(throwable: Throwable): Boolean {
    return throwable is ApolloNetworkException && !connectivityChecker.hasInternet()
  }
}
