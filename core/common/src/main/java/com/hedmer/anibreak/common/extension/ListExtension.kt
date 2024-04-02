package com.hedmer.anibreak.common.extension

fun <T> List<T>.replace(newValue: T, equal: Predicate<T>): List<T> {
  return map { current ->
    if (equal(current)) newValue else current
  }
}
