package com.hedmer.anibreak.common.network.di

import com.hedmer.anibreak.common.network.AnibreakDispatchers.Default
import com.hedmer.anibreak.common.network.AnibreakDispatchers.IO
import com.hedmer.anibreak.common.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineScopesModule {

  @[Provides Singleton ApplicationScope]
  fun providesDefaultCoroutineScope(
    @Dispatcher(Default) dispatcher: CoroutineDispatcher
  ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

  @[Provides Singleton ApplicationScope]
  fun providesIOCoroutineScope(
    @Dispatcher(IO) dispatcher: CoroutineDispatcher
  ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}