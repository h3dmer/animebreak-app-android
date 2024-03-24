package com.hedmer.anibreak

import android.app.Application
import com.hedmer.anibreak.sync.HomeMediaSyncInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AnibreakApp : Application() {

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    HomeMediaSyncInitializer(context = this)
  }
}
