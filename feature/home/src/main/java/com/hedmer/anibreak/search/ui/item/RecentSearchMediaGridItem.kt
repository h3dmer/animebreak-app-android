package com.hedmer.anibreak.search.ui.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.hedmer.anibreak.model.SearchBasicMedia

@Composable
fun RecentSearchMediaGridItem(
  item: SearchBasicMedia,
  modifier: Modifier
) {
  val imageLoader =
    rememberAsyncImagePainter(
      model = item.coverImage,
    )
  Card(
    modifier = modifier
      .size(150.dp)
      .padding(4.dp),
    shape = RoundedCornerShape(8.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 8.dp
    )
  ) {
    Image(
      painter = imageLoader,
      contentDescription = "",
      modifier = modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp)),
      contentScale = ContentScale.Crop
    )
  }
}
