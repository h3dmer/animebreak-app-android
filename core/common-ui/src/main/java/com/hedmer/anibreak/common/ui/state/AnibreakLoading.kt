package com.hedmer.anibreak.common.ui.state

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun AnibreakLoading() {
  val loadingTexts = listOf(
    "❀◕ ‿ ◕❀", "(ᅌᴗᅌ✿)", "٩(⊙‿⊙)۶", "ଘ(੭ˊᵕˋ)੭"
  )
  val ss by remember {
    derivedStateOf { loadingTexts.random() }
  }
  val label = "anibreakLoading"
  val infiniteTransition = rememberInfiniteTransition(
    label = label
  )
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = label
  )

  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
  ) {
    Text(
      text = ss,
      modifier = Modifier.graphicsLayer(rotationZ = rotation),
      style = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center)
    )
  }
}
