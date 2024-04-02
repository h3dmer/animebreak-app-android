package com.hedmer.anibreak.common.ui.extension

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.onLongClick(
  onClick: () -> Unit = {},
  onLongClick: () -> Unit
): Modifier {
  return combinedClickable(
    onLongClick = onLongClick,
    onClick = onClick
  )
}
