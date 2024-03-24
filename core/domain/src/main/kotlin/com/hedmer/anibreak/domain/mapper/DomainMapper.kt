package com.hedmer.anibreak.domain.mapper


interface DomainMapper<Data, DomainModel> {

  fun mapToDomain(data: Data): DomainModel
}
