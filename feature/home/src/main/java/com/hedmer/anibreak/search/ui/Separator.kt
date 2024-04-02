package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
private fun Separator() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
      .height(1.dp)
      .background(Color.LightGray)
  )
}

@Composable
fun AddSeparator(index: Int, size: Int) {
  if (index < size - 1) {
    Separator()
  }
}