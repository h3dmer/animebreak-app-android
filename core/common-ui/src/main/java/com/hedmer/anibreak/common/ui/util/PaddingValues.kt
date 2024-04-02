package com.hedmer.anibreak.common.ui.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun PaddingValues(
  horizontal: Dp = 0.dp,
  top: Dp = 0.dp,
  bottom: Dp = 0.dp
): PaddingValues = PaddingValues(
  start = horizontal,
  top = top,
  end = horizontal,
  bottom = bottom
)
