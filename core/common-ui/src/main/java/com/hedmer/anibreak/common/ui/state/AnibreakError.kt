package com.hedmer.anibreak.common.ui.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hedmer.anibreak.core.common.ui.R


@Composable
fun AnibreakErrorScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(bottom = 64.dp),
    contentAlignment = Alignment.Center
  ) {
    val composition by rememberLottieComposition(
      LottieCompositionSpec.RawRes(R.raw.error_anim_2)
    )

    val progress by animateLottieCompositionAsState(
      composition,
      iterations = LottieConstants.IterateForever,
      isPlaying = true,
      restartOnPlay = false
    )

    LottieAnimation(
      composition = composition,
      progress = { progress },
      modifier = Modifier
        .size(300.dp)
    )
  }
}