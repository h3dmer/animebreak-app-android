package com.hedmer.anibreak.common.network.di

import com.hedmer.anibreak.common.network.AnibreakDispatchers.Default
import com.hedmer.anibreak.common.network.AnibreakDispatchers.IO
import com.hedmer.anibreak.common.network.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

  @[Provides Dispatcher(IO)]
  fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

  @[Provides Dispatcher(Default)]
  fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
