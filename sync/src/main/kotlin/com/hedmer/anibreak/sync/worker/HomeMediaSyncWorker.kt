package com.hedmer.anibreak.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.hedmer.anibreak.domain.repository.HomeRepository
import com.hedmer.anibreak.sync.DelegatingWorker
import com.hedmer.anibreak.sync.delegatedData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class HomeMediaRefreshWorker @AssistedInject constructor(
  @Assisted context: Context,
  @Assisted workerParams: WorkerParameters,
  private val homeRepository: HomeRepository,
) : CoroutineWorker(context, workerParams) {

  override suspend fun doWork(): Result {
    return try {
      homeRepository.refreshHomeMediaFromNetwork()
      Result.success()
    } catch (e: Exception) {
      Result.failure()
    }
  }

  companion object {
    fun startUpWork() =
      PeriodicWorkRequestBuilder<DelegatingWorker>(
        REPEAT_TIME, TimeUnit.MINUTES
      )
        .setConstraints(constraints)
        .setInputData(
          HomeMediaRefreshWorker::class.delegatedData()
        )
        .build()

    private val constraints: Constraints =
      Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private const val REPEAT_TIME = 30L
  }
}
