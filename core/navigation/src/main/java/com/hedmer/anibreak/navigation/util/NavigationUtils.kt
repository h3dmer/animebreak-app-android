package com.hedmer.anibreak.navigation.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val SLIDE_DURATION = 350

fun NavGraphBuilder.slideComposable(
  route: String,
  arguments: List<NamedNavArgument> = emptyList(),
  duration: Int = SLIDE_DURATION,
  content: @Composable (AnimatedVisibilityScope.(NavBackStackEntry) -> Unit),
) {
  composable(
    route,
    arguments,
    enterTransition = {
      slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
        animationSpec = tween(duration),
      )
    },
    exitTransition = {
      slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
        animationSpec = tween(duration),
      )
    },
    popEnterTransition = {
      slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
        animationSpec = tween(duration),
      )
    },
    popExitTransition = {
      slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
        animationSpec = tween(duration),
      )
    },
  ) {
    content(it)
  }
}
