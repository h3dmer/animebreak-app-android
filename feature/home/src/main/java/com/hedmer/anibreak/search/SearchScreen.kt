package com.hedmer.anibreak.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hedmer.anibreak.common.error.InternetConnectivityException
import com.hedmer.anibreak.common.ui.state.AnibreakErrorScreen
import com.hedmer.anibreak.common.ui.state.AnibreakLoading
import com.hedmer.anibreak.common.ui.util.PaddingValues
import com.hedmer.anibreak.feature.home.R
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.model.search.FilterState
import com.hedmer.anibreak.model.search.RecentLocalSearches
import com.hedmer.anibreak.navigation.AnibreakScreens.MediaDetails.createRoute
import com.hedmer.anibreak.navigation.AppComposeNavigator
import com.hedmer.anibreak.search.ui.RecentSearches
import com.hedmer.anibreak.search.ui.SearchFiltersContent
import com.hedmer.anibreak.search.ui.SearchInput
import com.hedmer.anibreak.search.ui.item.RecentSearchMediaGridItem

@Composable
fun SearchRoute(
  composeNavigator: AppComposeNavigator,
  searchViewModel: SearchViewModel = hiltViewModel()
) {
  val searchUiState by searchViewModel.uiState.collectAsState()
  val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()
  val totalCounts = searchViewModel.totalItemsCount.collectAsStateWithLifecycle()
  val recentSearches = searchViewModel.recentSearchers.collectAsStateWithLifecycle()
  val searchFilter by searchViewModel.filters.collectAsStateWithLifecycle()
  val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()

  SearchScreen(
    searchUiState = searchUiState,
    searchQuery = searchQuery,
    onBackClick = { composeNavigator.navigateUp() },
    searchResults = searchResults,
    totalCounts = totalCounts,
    recentSearches = recentSearches,
    filters = searchFilter.filters,
    onSearchQueryChanged = searchViewModel::onSearchQueryChange,
    onSearchTriggered = searchViewModel::onSearchTriggered,
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
  searchQuery: String,
  onSearchTriggered: (String) -> Unit,
  onBackClick: () -> Unit,
  searchResults: LazyPagingItems<SearchBasicMedia>,
  totalCounts: State<Int?>,
  recentSearches: State<RecentLocalSearches?>,
  filters: List<FilterState>,
  onSearchQueryChanged: (String) -> Unit,
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
    SearchInput(
      modifier = Modifier
        .padding(horizontal = 16.dp),
      searchQuery = searchQuery,
      onSearchQueryChanged = onSearchQueryChanged,
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
          filters = filters,
          onMediaClick = onSearchMediaClick
        )
      }
    }
  }
}

@Composable
fun SearchSuccessContent(
  pagingItems: LazyPagingItems<SearchBasicMedia>,
  totalItems: State<Int?>,
  filters: List<FilterState>,
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
        (pagingItems.loadState.refresh as LoadState.Error).let { errorState ->
          when(errorState.error) {
            is InternetConnectivityException -> {
              AnibreakErrorScreen()
            }
            else -> {
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
        }
      }
      is LoadState.NotLoading -> {
        if (filters.isNotEmpty()) {
          SearchFiltersContent(filters)
        }

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
              RecentSearchMediaGridItem(
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
