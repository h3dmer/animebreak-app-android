package com.hedmer.anibreak.domain.mapper

sealed class DomainResult<out T> {

  data class Success<T>(val data: T) : DomainResult<T>()
  data class Error(val cause: Throwable? = null) : DomainResult<Nothing>()
}
