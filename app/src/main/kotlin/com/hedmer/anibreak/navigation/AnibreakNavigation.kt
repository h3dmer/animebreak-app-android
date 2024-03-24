package com.hedmer.anibreak.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hedmer.anibreak.animedetails.MediaDetailsRoute
import com.hedmer.anibreak.home.HomeRoute
import com.hedmer.anibreak.navigation.util.slideComposable
import com.hedmer.anibreak.search.SearchRoute

fun NavGraphBuilder.anibreakHomeNavigation(
  composeNavigator: AppComposeNavigator
) {

  composable(
    route = AnibreakScreens.Home.name,
  ) {
    HomeRoute(composeNavigator)
  }

  composable(
    route = AnibreakScreens.MediaDetails.name,
    arguments = AnibreakScreens.MediaDetails.navArguments
  ) {
    MediaDetailsRoute(
      anibreakNavigator = composeNavigator
    )
  }

  slideComposable(
    route = AnibreakScreens.Search.name,
  ) {
    SearchRoute(composeNavigator)
  }
}
