package com.hedmer.anibreak.home

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hedmer.anibreak.common.ui.state.AnibreakErrorScreen
import com.hedmer.anibreak.model.BasicMediaDetails
import com.hedmer.anibreak.feature.home.R
import com.hedmer.anibreak.navigation.AnibreakScreens
import com.hedmer.anibreak.navigation.AnibreakScreens.MediaDetails.createRoute
import com.hedmer.anibreak.navigation.AppComposeNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.absoluteValue

@Composable
fun HomeRoute(
  composeNavigator: AppComposeNavigator,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val items by viewModel.state.collectAsStateWithLifecycle()

  HomeScreen(
    items = items,
    isRefreshing = viewModel.isRefreshing,
    onSearchBarClick = {
      composeNavigator.navigate(AnibreakScreens.Search.route)
    },
    onMediaClick = {
      composeNavigator.navigate(createRoute(it.toString()))
    },
    refreshHomeData = { viewModel.invalidateHomeScreen() },
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeScreen(
  items: HomeUiState,
  isRefreshing: Boolean,
  onSearchBarClick: () -> Unit,
  onMediaClick: (Int) -> Unit,
  refreshHomeData: () -> Unit
) {
  val pullRefreshState = rememberPullRefreshState(
    refreshing = isRefreshing,
    onRefresh = { refreshHomeData() }
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      modifier = Modifier
        .statusBarsPadding()
        .pullRefresh(pullRefreshState)

    ) {

      CustomSearchBar { onSearchBarClick() }

      items.let { state ->
        when (state) {
          is HomeUiState.Success -> {
            HomeScreenSuccess(
              state = state,
              animeDetailsClick = onMediaClick
            )
          }

          HomeUiState.Loading -> LoadingScreen()

          HomeUiState.Error -> AnibreakErrorScreen()
        }
      }
    }
    PullRefreshIndicator(
      refreshing = isRefreshing,
      state = pullRefreshState,
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 48.dp)
    )
  }
}

@Composable
fun HomeScreenSuccess(
  state: HomeUiState.Success,
  animeDetailsClick: (Int) -> Unit
) {
  Column(
    modifier = Modifier
      .statusBarsPadding()
  ) {
    HomeScrollableContent(
      state = state,
      animeDetailsClick = animeDetailsClick
    )
  }
}

@Composable
private fun HomeScrollableContent(
  state: HomeUiState.Success,
  animeDetailsClick: (Int) -> Unit
) {
  val scrollState = rememberScrollState()
  Column(
    modifier = Modifier
      .verticalScroll(scrollState)
  ) {
    HomeAnimeHorizontalPager(
      state.homeAnimeData,
      animeDetailsClick
    )
    AnimeHorizontalList(
      headlineRes = R.string.home_trending_now,
      animeDetailsClick = animeDetailsClick,
      animeList = state.homeAnimeData.trendingNow
    )
    AnimeHorizontalList(
      headlineRes = R.string.home_popular_this_season,
      animeList = state.homeAnimeData.popularThisSeason,
      animeDetailsClick = animeDetailsClick,
      modifier = Modifier.padding(vertical = 8.dp)
    )
    AnimeHorizontalList(
      headlineRes = R.string.home_upcoming_next_season,
      animeDetailsClick = animeDetailsClick,
      animeList = state.homeAnimeData.upcomingNextSeason
    )
    AnimeHorizontalList(
      headlineRes = R.string.home_all_time_popular,
      animeList = state.homeAnimeData.allTimePopular,
      animeDetailsClick = animeDetailsClick,
      modifier = Modifier.padding(vertical = 8.dp)
    )
    AnimeHorizontalList(
      headlineRes = R.string.home_top_ten,
      animeDetailsClick = animeDetailsClick,
      animeList = state.homeAnimeData.topTen,
      modifier = Modifier.padding(bottom = 48.dp)
    )
  }
}

@Composable
fun AnimeHorizontalList(
  @StringRes headlineRes: Int,
  animeList: List<BasicMediaDetails>,
  animeDetailsClick: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  if (animeList.isNotEmpty()) {
    Column(
      modifier = modifier
    ) {
      Text(
        text = stringResource(id = headlineRes),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
      )
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(animeList) { anime ->
          AnimeItem(anime, animeDetailsClick)
        }
      }
    }
  }
}

@Composable
fun AnimeItem(
  anime: BasicMediaDetails,
  animeDetailsClick: (Int) -> Unit
) {
  Column(
    modifier = Modifier.width(150.dp)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp),
      onClick = { animeDetailsClick(anime.id) }
    ) {
      val imageLoader =
        rememberAsyncImagePainter(
          model = anime.coverImage?.extraLarge,
        )
      Box {
        Image(
          painter = imageLoader,
          contentDescription = anime.title,
          contentScale = ContentScale.FillBounds,
          modifier = Modifier
            .fillMaxSize()
        )
      }
    }
    Text(
      text = anime.title,
      maxLines = 2,
      style = MaterialTheme.typography.labelMedium,
      modifier = Modifier.padding(vertical = 4.dp)
    )
  }
}

@Composable
fun LoadingScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentSize(Alignment.Center)
  ) {
    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeAnimeHorizontalPager(
  homeAnimeContentState: HomeAnimeContentState,
  animeDetailsClick: (Int) -> Unit
) {
  val items = homeAnimeContentState.trendingNow
  val pageCount = items.size
  val pagerState = rememberPagerState(
    initialPage = pageCount / 2
  ) {
    pageCount
  }

  val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
  if (isDragged.not()) {
    with(pagerState) {
      if (pageCount > 0) {
        var currentPageKey by remember { mutableIntStateOf(0) }
        LaunchedEffect(key1 = currentPageKey) {
          launch {
            delay(timeMillis = 3000L)
            val nextPage = (currentPage + 1).mod(pageCount)
            animateScrollToPage(
              page = nextPage,
              animationSpec = tween(
                durationMillis = 1300
              )
            )
            currentPageKey = nextPage
          }
        }
      }
    }
  }

  HorizontalPager(
    state = pagerState,
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp),
    contentPadding = PaddingValues(
      horizontal = 48.dp
    ),
    pageSpacing = 16.dp
  ) { page ->

    val mediaDetails = homeAnimeContentState.trendingNow[page]

    val painter: Painter = rememberAsyncImagePainter(
      model = ImageRequest.Builder(LocalContext.current)
        .data(mediaDetails.bannerImage ?: "")
        .crossfade(true)
        .build()
    )

    Image(
      painter = painter,
      contentDescription = mediaDetails.title,
      modifier = Modifier
        .carouselTransition(page, pagerState)
        .fillMaxSize()
        .clip(RoundedCornerShape(16.dp))
        .clickable {
          animeDetailsClick(mediaDetails.id)
        },
      contentScale = ContentScale.Crop
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
  graphicsLayer {
    val pageOffset =
      ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation =
      lerp(
        start = 0.9f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
      )
    alpha = transformation
    scaleY = transformation
  }


@Composable
fun CustomSearchBar(onSearchBarClicked: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onSearchBarClicked)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search Icon"
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Search for anime/manga"
      )
    }
  }
}
