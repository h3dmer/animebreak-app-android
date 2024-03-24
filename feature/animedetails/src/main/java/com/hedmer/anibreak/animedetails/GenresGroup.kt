@file:OptIn(ExperimentalLayoutApi::class)

package com.hedmer.anibreak.animedetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenresGroup(
  genres: List<String>,
  modifier: Modifier
) {
  FlowRow(
    horizontalArrangement = Arrangement.spacedBy(3.dp),
    verticalArrangement = Arrangement.spacedBy(3.dp),
    modifier = modifier
  ) {
    genres.forEach { chipText ->
      GenreChip(text = chipText)
    }
  }
}

@Composable
private fun GenreChip(text: String) {
  Box(
    modifier = Modifier
      .background(
        color = Color(0xFF333333),
        shape = RoundedCornerShape(50)
      )
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp
      ),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      color = Color.White,
      fontSize = 12.sp,
      textAlign = TextAlign.Center
    )
  }
}
