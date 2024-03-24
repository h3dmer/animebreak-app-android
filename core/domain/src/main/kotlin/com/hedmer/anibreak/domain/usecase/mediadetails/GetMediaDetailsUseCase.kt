package com.hedmer.anibreak.domain.usecase.mediadetails

import com.hedmer.anibreak.domain.mapper.DomainResult
import com.hedmer.anibreak.domain.repository.HomeRepository
import com.hedmer.anibreak.model.MediaDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaDetailsUseCase @Inject constructor(
  private val homeRepository: HomeRepository
) {

  operator fun invoke(id: Int?): Flow<DomainResult<MediaDetails>> {
    return homeRepository.getMedialDetails(id)
  }
}
