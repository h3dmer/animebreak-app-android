package com.hedmer.anibreak.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val anibreakDispatchers: AnibreakDispatchers)

enum class AnibreakDispatchers {
  Default,
  IO,
}
