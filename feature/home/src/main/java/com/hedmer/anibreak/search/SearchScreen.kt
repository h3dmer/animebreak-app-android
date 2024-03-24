package com.hedmer.anibreak.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hedmer.anibreak.common.ui.state.AnibreakErrorScreen
import com.hedmer.anibreak.common.ui.state.AnibreakLoading
import com.hedmer.anibreak.feature.home.R
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.model.search.RecentLocalSearches
import com.hedmer.anibreak.navigation.AnibreakScreens.MediaDetails.createRoute
import com.hedmer.anibreak.navigation.AppComposeNavigator
import timber.log.Timber

@Composable
fun SearchRoute(
  composeNavigator: AppComposeNavigator,
  searchViewModel: SearchViewModel = hiltViewModel()
) {
  val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
  val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()
  val totalCounts = searchViewModel.totalItemsCount.collectAsStateWithLifecycle()
  val recentSearches = searchViewModel.recentSearchers.collectAsStateWithLifecycle()

  SearchScreen(
    searchUiState = searchUiState,
    onBackClick = { composeNavigator.navigateUp() },
    onSearchTriggered = searchViewModel::onSearchTriggered,
    searchResults = searchResults,
    totalCounts = totalCounts,
    recentSearches = recentSearches,
    applyRecentSearch = searchViewModel::onSearchTriggered,
    removeRecentSearch = searchViewModel::onRemoveSearch,
    onSearchMediaClick = { media ->
      searchViewModel.onMediaSearchClick(media)
      composeNavigator.navigate(createRoute(media.id.toString()))
    },
    removeRecentMediaList = {
      searchViewModel.onRemoveRecentMediaList()
    },
    onClearContent = { searchViewModel.onClearContent() },
    onRecentMediaClick = {
      composeNavigator.navigate(createRoute(it.toString()))
    }
  )
}

@Composable
private fun SearchScreen(
  modifier: Modifier = Modifier,
  searchUiState: SearchUiState,
  onSearchTriggered: (String) -> Unit,
  onBackClick: () -> Unit,
  searchResults: LazyPagingItems<SearchBasicMedia>,
  totalCounts: State<Int?>,
  recentSearches: State<RecentLocalSearches?>,
  applyRecentSearch: (String) -> Unit,
  removeRecentSearch: (String) -> Unit,
  onSearchMediaClick: (SearchBasicMedia) -> Unit,
  onRecentMediaClick: (Int) -> Unit,
  removeRecentMediaList: () -> Unit,
  onClearContent: () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color.White)
      .statusBarsPadding()
      .padding(top = 12.dp)
  ) {
    CustomSearchBar(
      modifier = Modifier
        .padding(horizontal = 16.dp),
      onSearchTriggered = onSearchTriggered,
      onBackClick = onBackClick,
      isClearVisible = searchUiState is SearchUiState.Success,
      clearContent = onClearContent
    )
    when (searchUiState) {
      is SearchUiState.EmptyQuery -> {
        EmptyQueryScreen(
          recentSearches = recentSearches,
          applyRecentSearch = applyRecentSearch,
          removeRecentSearch = removeRecentSearch,
          removeRecentMediaList = removeRecentMediaList,
          onMediaClick = onRecentMediaClick
        )
      }
      SearchUiState.Loading -> {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
          )
        }
      }
      SearchUiState.LoadFailed -> {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "Load failed")
        }
      }
      is SearchUiState.Success -> {
        SearchSuccessContent(
          pagingItems = searchResults,
          totalItems = totalCounts,
          onMediaClick = onSearchMediaClick
        )
      }
    }
  }
}

@Composable
fun CustomSearchBar(
  modifier: Modifier = Modifier,
  hint: String = "Search: anime/manga",
  isClearVisible: Boolean,
  onSearchTriggered: (String) -> Unit,
  onBackClick: () -> Unit,
  clearContent: () -> Unit
) {
  val focusRequester = remember { FocusRequester() }
  val searchText = rememberSaveable {
    mutableStateOf("")
  }
  val keyboardController = LocalSoftwareKeyboardController.current

  val onSearchImeClick = {
    keyboardController?.hide()
    onSearchTriggered(searchText.value)
  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp) // Typically material design height for an app bar
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
  ) {
    TextField(
      value = searchText.value,
      onValueChange = {
        searchText.value = it
      },
      modifier = Modifier
        .fillMaxHeight()
        .padding(start = 16.dp, end = 16.dp)
        .align(Alignment.CenterStart)
        .focusRequester(focusRequester)
        .onKeyEvent {
          if (it.key == Key.Enter) {
            onSearchTriggered(searchText.value)
            true
          } else {
            false
          }
        },
      placeholder = { Text(text = hint, color = MaterialTheme.colorScheme.onSurfaceVariant) },
      colors = TextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
      ),
      textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
      singleLine = true,
      leadingIcon = {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.clickable {
            onBackClick()
          }
        )
      },
      trailingIcon = {
        if (isClearVisible) {
          Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "Clear",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.clickable {
              searchText.value = ""
              clearContent()
            }
          )
        }
      },
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search,
      ),
      keyboardActions = KeyboardActions(
        onSearch = {
          onSearchImeClick()
        },
      ),
    )
  }
}

@Composable
fun SearchSuccessContent(
  pagingItems: LazyPagingItems<SearchBasicMedia>,
  totalItems: State<Int?>,
  onMediaClick: (SearchBasicMedia) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
  ) {
    when (pagingItems.loadState.refresh) {
      is LoadState.Loading -> {
        AnibreakLoading()
      }
      is LoadState.Error -> {
        AnibreakErrorScreen()

      }
      is LoadState.NotLoading -> {
        totalItems.value?.let {
          Text(
            text = "Total counts: $it",
            modifier = Modifier.padding(16.dp)
          )
        }

        LazyVerticalGrid(
          columns = GridCells.Fixed(3),
          contentPadding = PaddingValues(
            horizontal = 8.dp,
            top = 24.dp,
            bottom = 48.dp
          ),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = modifier
        ) {
          items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id }
          ) { index ->
            pagingItems[index]?.let { media ->
              MediaGridItem(
                item = media,
                modifier = Modifier.clickable {
                  onMediaClick(media)
                }
              )
            }
          }

          when (pagingItems.loadState.append) {
            is LoadState.Error -> {
              item(span = { GridItemSpan(maxLineSpan) }) {
                Box(modifier = Modifier.fillMaxSize()) {
                  AnibreakErrorScreen()
                }
              }
            }
            is LoadState.Loading -> {
              item(span = { GridItemSpan(maxLineSpan) }) {
                ShowLoader()
              }
            }
            else -> {}
          }
        }
      }
    }
  }
}

@Composable
private fun ShowLoader() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator(
      modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
    )
  }
}

@Composable
private fun EmptyQueryScreen(
  recentSearches: State<RecentLocalSearches?>,
  applyRecentSearch: (String) -> Unit,
  removeRecentSearch: (String) -> Unit,
  removeRecentMediaList: () -> Unit,
  onMediaClick: (Int) -> Unit
) {
  if (recentSearches.value?.anyAvailable() == true) {
    recentSearches.value?.let { searches ->
      RecentSearches(
        recentSearches = searches,
        applyRecentSearch = applyRecentSearch,
        removeRecentSearch = removeRecentSearch,
        removeRecentMediaList = removeRecentMediaList,
        onMediaClick = onMediaClick
      )
    }
  } else {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 64.dp),
      contentAlignment = Alignment.Center
    ) {
      EmptySearch()
    }
  }
}

@Composable
private fun RecentSearches(
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
            start = 24.dp,
            end = 24.dp,
            top = 24.dp,
            bottom = 8.dp
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
              contentDescription = "Clock"
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
              contentDescription = "Remove",
              modifier = Modifier
                .padding(16.dp)
                .clickable { removeRecentSearch(item.query) },
              tint = Color.Gray
            )
          }

          if (index < queries.size - 1) {
            Separator()
          }
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

@Composable
private fun MediaGridItem(
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

@Composable
fun EmptySearch() {
  val composition by rememberLottieComposition(
    LottieCompositionSpec.RawRes(R.raw.empty_search_animation)
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

@Composable
fun Separator() {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp)
      .height(1.dp)
      .background(Color.LightGray)
  )
}

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
