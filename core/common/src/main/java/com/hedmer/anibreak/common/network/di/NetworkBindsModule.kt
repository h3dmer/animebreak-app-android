package com.hedmer.anibreak.common.network.di

import com.hedmer.anibreak.common.network.AnibreakInternetConnectivityChecker
import com.hedmer.anibreak.common.network.InternetConnectivityChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

  @Binds
  abstract fun bindInternetConnectivityChecker(
    anibreakInternetConnectivityChecker: AnibreakInternetConnectivityChecker
  ): InternetConnectivityChecker
}