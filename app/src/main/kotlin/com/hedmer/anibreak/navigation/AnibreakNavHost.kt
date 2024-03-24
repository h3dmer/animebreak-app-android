package com.hedmer.anibreak.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AnibreakNavHost(
  navHostController: NavHostController,
  composeNavigator: AppComposeNavigator
) {
  NavHost(
    navController = navHostController,
    startDestination = AnibreakScreens.Home.route
  ) {
    anibreakHomeNavigation(
      composeNavigator = composeNavigator
    )
  }
}
