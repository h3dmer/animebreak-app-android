package com.hedmer.anibreak.core.database

import com.hedmer.anibreak.core.database.datasource.home.DBHomeDataSource
import com.hedmer.anibreak.core.database.datasource.home.DatabaseHomeDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceBindModule {

  @Binds
  internal abstract fun bindDBHomeDataSource(
    databaseHomeDataSource: DatabaseHomeDataSource
  ): DBHomeDataSource
}
