package com.hedmer.anibreak.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView


private val LightDefaultColorScheme = lightColorScheme(
  primary = STREAM_PRIMARY,
  primaryContainer = STREAM_PRIMARY,
  secondary = STREAM_PRIMARY,
  background = STREAM_PRIMARY,
  tertiary = STREAM_PRIMARY,
  onTertiary = GRAY200
)

private val DarkDefaultColorScheme = darkColorScheme(
  primary = STREAM_PRIMARY,
  primaryContainer = STREAM_PRIMARY,
  secondary = STREAM_PRIMARY,
  background = STREAM_PRIMARY,
  tertiary = STREAM_PRIMARY,
  onTertiary = GRAY200
)



/** Light Android background theme */
private val LightAndroidBackgroundTheme = BackgroundTheme(color = Color.White)

/** Dark Android background theme */
private val DarkAndroidBackgroundTheme = BackgroundTheme(color = DARK_BACKGROUND_PRIMARY)

@Composable
fun AnibreakComposeTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
  val backgroundTheme = if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window

      window.statusBarColor = Color.Transparent.toArgb()
//      window.isNavigationBarContrastEnforced = false
    }
  }

  CompositionLocalProvider(
    LocalBackgroundTheme provides backgroundTheme
  ) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = AnibreakTypography,
      content = content
    )
  }
}
