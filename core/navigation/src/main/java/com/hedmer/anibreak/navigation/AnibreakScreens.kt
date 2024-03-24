package com.hedmer.anibreak.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class AnibreakScreens(
  val route: String,
  val navArguments: List<NamedNavArgument> = emptyList()
) {
  val name: String = route.appendArguments(navArguments)

  data object Home : AnibreakScreens("anibreak_home")

  data object MediaDetails : AnibreakScreens(
    route = "anibreak_media_details",
    navArguments = listOf(navArgument(ARGUMENT_MEDIA_DETAILS_ID) { type = NavType.StringType })
  ) {
    fun createRoute(mediaId: String) =
      name.replace("{${navArguments.first().name}}", mediaId)
  }

  data object Search : AnibreakScreens("anibreak_search")


  companion object {
    const val ARGUMENT_MEDIA_DETAILS_ID = "media_details_id"
  }
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
  val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
    .takeIf { it.isNotEmpty() }
    ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
    .orEmpty()
  val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
    .takeIf { it.isNotEmpty() }
    ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
    .orEmpty()
  return "$this$mandatoryArguments$optionalArguments"
}
