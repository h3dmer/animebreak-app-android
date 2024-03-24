package com.hedmer.anibreak.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.hedmer.anibreak.navigation.AnibreakNavHost
import com.hedmer.anibreak.navigation.AppComposeNavigator
import com.hedmer.anibreak.designsystem.component.AnibreakBackground
import com.hedmer.anibreak.designsystem.theme.AnibreakComposeTheme

@Composable
fun AnibreakMain(
  composeNavigator: AppComposeNavigator
) {
  AnibreakComposeTheme {
    val navHostController = rememberNavController()

    LaunchedEffect(Unit) {
      composeNavigator.handleNavigationCommands(navHostController)
    }

    AnibreakBackground {
      AnibreakNavHost(navHostController = navHostController, composeNavigator = composeNavigator)
    }
  }
}
