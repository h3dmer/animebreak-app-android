package com.hedmer.anibreak.core.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val ANIBREAK_DATABASE_NAME = "anibreak-database"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

  @[Provides Singleton]
  fun providesAnibreakDatabase(
    @ApplicationContext context: Context,
  ): AnibreakDatabase = Room.databaseBuilder(
    context,
    AnibreakDatabase::class.java,
    ANIBREAK_DATABASE_NAME,
  )
    .fallbackToDestructiveMigration()
    .build()
}
