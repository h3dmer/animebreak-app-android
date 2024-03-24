package com.hedmer.anibreak.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.hedmer.anibreak.sync.worker.HomeMediaRefreshWorker

internal const val HOME_MEDIA_SYNC_WORK = "HomeMediaSyncWork"

object HomeMediaSyncInitializer {

  operator fun invoke(context: Context) {
    WorkManager.getInstance(context).apply {
      enqueueUniquePeriodicWork(
        HOME_MEDIA_SYNC_WORK,
        ExistingPeriodicWorkPolicy.KEEP,
        HomeMediaRefreshWorker.startUpWork(),
      )
    }
  }
}
