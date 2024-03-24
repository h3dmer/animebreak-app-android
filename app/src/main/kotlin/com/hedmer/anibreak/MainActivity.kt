package com.hedmer.anibreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.hedmer.anibreak.navigation.AppComposeNavigator
import com.hedmer.anibreak.ui.AnibreakMain
import com.hedmer.anibreak.designsystem.theme.AnibreakComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  internal lateinit var appComposeNavigator: AppComposeNavigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      CompositionLocalProvider {
        AnibreakComposeTheme { AnibreakMain(composeNavigator = appComposeNavigator) }
      }
    }
  }
}
