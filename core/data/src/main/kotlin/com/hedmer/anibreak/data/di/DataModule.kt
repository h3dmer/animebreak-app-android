package com.hedmer.anibreak.data.di

import com.hedmer.anibreak.data.error.AnibreakErrorHandler
import com.hedmer.anibreak.data.error.StandardGraphqlErrorHandler
import com.hedmer.anibreak.data.repository.DefaultHomeRepository
import com.hedmer.anibreak.data.repository.SearchDefaultRepository
import com.hedmer.anibreak.domain.repository.HomeRepository
import com.hedmer.anibreak.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

  @Binds
  internal abstract fun bindHomeRepository(
    defaultHomeRepository: DefaultHomeRepository
  ): HomeRepository

  @Binds
  internal abstract fun bindSearchRepository(
    searchDefaultRepository: SearchDefaultRepository
  ): SearchRepository

  @Binds
  internal abstract fun bindGraphqlErrorHandler(
    standardGraphqlErrorHandler: StandardGraphqlErrorHandler
  ): AnibreakErrorHandler
}
