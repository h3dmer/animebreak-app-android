package com.hedmer.anibreak.core.network.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import com.hedmer.anibreak.common.network.AnibreakDispatchers
import com.hedmer.anibreak.common.network.Dispatcher
import com.hedmer.anibreak.core.network.interceptor.ErrorInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

  @[Provides Singleton]
  fun provideApolloClient(
    errorInterceptor: ErrorInterceptor,
    @Dispatcher(AnibreakDispatchers.IO) ioDispatcher: CoroutineDispatcher
  ): ApolloClient = ApolloClient.Builder()
    .dispatcher(ioDispatcher)
    .serverUrl(ANILIST_SERVER_URL)
    .addHttpInterceptor(LoggingInterceptor(LoggingInterceptor.Level.BODY))
    .okHttpClient(
      OkHttpClient()
        .newBuilder()
        .addInterceptor(OkHttpProfilerInterceptor()).build()
    )
    .addInterceptor(errorInterceptor)
    .build()

  private companion object {
     const val ANILIST_SERVER_URL = "https://graphql.anilist.co"
  }
}
