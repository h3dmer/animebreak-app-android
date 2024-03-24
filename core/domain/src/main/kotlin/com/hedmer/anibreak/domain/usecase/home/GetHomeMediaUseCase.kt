package com.hedmer.anibreak.domain.usecase.home

import com.hedmer.anibreak.common.util.Result
import com.hedmer.anibreak.domain.repository.HomeRepository
import com.hedmer.anibreak.model.HomeMedia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHomeMediaUseCase @Inject constructor(
  private val homeRepository: HomeRepository
) {

  operator fun invoke(): Flow<Result<HomeMedia>> {
    return homeRepository.observeHomeMedia()
  }
}
