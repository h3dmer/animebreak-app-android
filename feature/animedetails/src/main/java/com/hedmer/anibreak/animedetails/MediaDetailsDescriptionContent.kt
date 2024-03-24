package com.hedmer.anibreak.animedetails

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
internal fun MediaDetailsDescriptionContent(
  description: String,
  modifier: Modifier = Modifier
) {
  val (extended, setExtended) = remember {
    mutableStateOf(false)
  }
  val text = remember(description) {
    Html.fromHtml(
      /* source = */ description,
      /* flags = */ HtmlCompat.FROM_HTML_MODE_LEGACY
    )
  }
  val bottomShadow = Color.Transparent.takeIf { extended }
    ?: Color.White.copy(alpha = 0.9f)
  Box(
    modifier = modifier
      .animateContentSize(animationSpec = tween(durationMillis = 300))
      .let {
        if (!extended) it.heightIn(max = 100.dp) else it
      },
  ) {
    AndroidView(
      factory = { context ->
        TextView(context).apply {
          movementMethod = LinkMovementMethod.getInstance()
          textSize = 14f
        }
      },
      update = { it.text = text },
      modifier = Modifier
        .padding(top = 8.dp)
    )
    Box(
      modifier = Modifier
        .matchParentSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color.Transparent,
              bottomShadow
            )
          )
        )
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
        ) {
          setExtended(!extended)
        }
    )
  }
}
