package com.hedmer.anibreak.model.search


data class MediaPageInfo(
  val total: Int?,
  val perPage: Int?,
  val currentPage: Int?,
  val lastPage: Int?,
  val hasNextPage: Boolean?
)
