package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hedmer.anibreak.common.ui.extension.onLongClick
import com.hedmer.anibreak.model.search.RecentLocalSearches

@Composable
fun RecentSearches(
  recentSearches: RecentLocalSearches,
  applyRecentSearch: (String) -> Unit,
  removeRecentSearch: (String) -> Unit,
  removeRecentMediaList: () -> Unit,
  onMediaClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val queries = recentSearches.queries
  val mediaList = recentSearches.media
  Column(
    modifier = modifier
  ) {
    if (!queries.isNullOrEmpty()) {
      Text(
        text = "Recent queries",
        fontSize = 18.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp
          ),
      )
      LazyColumn(
        modifier = Modifier
          .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
      ) {
        itemsIndexed(
          items = queries,
          key = { index, _ -> index }
        ) { index, item ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(horizontal = 8.dp)
              .clickable { applyRecentSearch(item.query) }
          ) {
            Icon(
              imageVector = Icons.Filled.Search,
              contentDescription = "Search"
            )
            Text(
              text = item.query,
              modifier = Modifier.padding(horizontal = 16.dp)
            )
            Box(
              modifier = Modifier.weight(1f, true)
            )
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close",
              modifier = Modifier
                .padding(16.dp)
                .clickable { removeRecentSearch(item.query) },
              tint = Color.Gray
            )
          }

          AddSeparator(index, queries.size)
        }
      }
    }
    if (!mediaList.isNullOrEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Recent media search",
        fontSize = 18.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
          .onLongClick { removeRecentMediaList() }
      )
      LazyRow(
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(mediaList, key = { it.id }) { item ->
          val imageLoader =
            rememberAsyncImagePainter(
              model = item.coverImage,
            )
          Card(
            modifier = modifier
              .height(200.dp)
              .width(130.dp)
              .clickable { onMediaClick(item.id) },
            elevation = CardDefaults.cardElevation(
              defaultElevation = 4.dp
            )
          ) {
            Image(
              painter = imageLoader,
              contentDescription = "",
              modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp)),
              contentScale = ContentScale.Crop
            )
          }
        }
      }
    }
  }
}
