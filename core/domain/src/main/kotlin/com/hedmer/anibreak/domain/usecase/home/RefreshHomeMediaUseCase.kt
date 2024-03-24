package com.hedmer.anibreak.domain.usecase.home

import com.hedmer.anibreak.domain.repository.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshHomeMediaUseCase @Inject constructor(
  private val homeRepository: HomeRepository
) {

  suspend operator fun invoke() =
    homeRepository.refreshHomeMediaFromNetwork()
}
