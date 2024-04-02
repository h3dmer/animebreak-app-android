package com.hedmer.anibreak.data.mappers

import com.hedmer.anibreak.core.network.model.SearchRequestParam
import com.hedmer.anibreak.model.search.SearchParam
import type.MediaFormat
import type.MediaSeason
import type.MediaType

fun SearchParam.toRequestParam(): SearchRequestParam {
  return SearchRequestParam(
    page = page,
    query = query,
    type = type?.let {
      MediaType.valueOf(it.uppercase())
    },
    season = season?.let {
      MediaSeason.valueOf(it.uppercase())
    },
    format = format?.let {
      MediaFormat.valueOf(it.uppercase())
    },
    year = year?.toIntOrNull()
  )
}
