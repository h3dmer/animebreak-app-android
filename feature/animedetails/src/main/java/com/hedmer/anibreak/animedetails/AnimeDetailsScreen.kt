package com.hedmer.anibreak.animedetails

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.hedmer.anibreak.animedetails.MediaDetailsIntent.OnScreenCreated
import com.hedmer.anibreak.common.ui.state.AnibreakErrorScreen
import com.hedmer.anibreak.common.ui.state.AnibreakLoading
import com.hedmer.anibreak.model.MediaDetails
import com.hedmer.anibreak.model.MediaParticipant
import com.hedmer.anibreak.navigation.AppComposeNavigator
import timber.log.Timber

@Composable
fun MediaDetailsRoute(
  anibreakNavigator: AppComposeNavigator,
) {
  MediaDetailsScreen(
    onBackPressed = { anibreakNavigator.navigateUp() }
  )
}

@Composable
internal fun MediaDetailsScreen(
  modifier: Modifier = Modifier,
  onBackPressed: () -> Unit,
  viewModel: MediaDetailsViewModel = hiltViewModel(),
) {
  val state = viewModel.uiState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.processIntent(OnScreenCreated)
  }

  MediaDetailsScreenContent(
    modifier = modifier,
    onBackPressed,
    state.value
  )
}

@Composable
internal fun MediaDetailsScreenContent(
  modifier: Modifier = Modifier,
  onBackPressed: () -> Unit,
  mediaDetailsUiState: MediaDetailsUiState = MediaDetailsUiState.Loading
) {

  mediaDetailsUiState.let { mediaDetailsState ->
    when (mediaDetailsState) {
      is MediaDetailsUiState.Success -> {
        NewMediaDetails(
          mediaDetails = mediaDetailsState.mediaDetails,
          onBackPressed = onBackPressed
        )
      }

      is MediaDetailsUiState.Error -> {
        AnibreakErrorScreen()
      }

      else -> {
        AnibreakLoading()
      }
    }
  }
}

@Composable
private fun MediaImageSmall(
  imageUrl: String?,
  modifier: Modifier = Modifier,
) {
  val imageLoader =
    rememberAsyncImagePainter(
      model = imageUrl,
    )
  Box(
    modifier = modifier
      .width(160.dp)
      .height(220.dp)
      .clip(RoundedCornerShape(16.dp))
  ) {
    Image(
      painter = imageLoader,
      contentDescription = null,
      contentScale = ContentScale.FillBounds,
      modifier = Modifier
        .fillMaxSize()
    )
  }
}

@Composable
private fun MediaYoutubeVideo(
  modifier: Modifier
) {
  AndroidView(
    factory = { context ->
      WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        webViewClient = WebViewClient()
        settings.javaScriptEnabled = true
        loadUrl("https://www.youtube.com/embed/luYOt2-c2TI")
      }
    },
    modifier = modifier
  )
}

@Composable
private fun NewMediaDetails(
  mediaDetails: MediaDetails,
  onBackPressed: () -> Unit
) {
  val scrollState = rememberScrollState()
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(400.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
      ) {
        val imageLoader =
          rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
              .transformations(BlurTransformation(LocalContext.current))
              .data(mediaDetails.coverImage?.extraLarge)
              .build()
          )
        Image(
          painter = imageLoader,
          contentDescription = mediaDetails.title.toString(),
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        Box(
          modifier = Modifier
            .matchParentSize()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  Color.White.copy(alpha = 0.5f),
                  Color.Transparent,
                  Color.Transparent,
                  Color.Transparent,
                  Color.White.copy(alpha = 0.5f),
                  Color.White
                )
              )
            )
        )
      }
      MediaImageSmall(
        mediaDetails.coverImage?.extraLarge,
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 48.dp)
      )
      CloseIconButton(
        modifier = Modifier
          .padding(top = 48.dp, start = 24.dp)
          .align(Alignment.TopStart),
        onClick = onBackPressed
      )
    }

    Text(
      text = mediaDetails.title?.getAnyNameOrEmpty() ?: "",
      style = MaterialTheme.typography.headlineLarge,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )

    GenresGroup(
      genres = mediaDetails.genres,
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(vertical = 8.dp, horizontal = 16.dp)
    )

    if (mediaDetails.description.isNotEmpty()) {
      Text(
        text = "Synopsis",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .align(Alignment.Start)
          .padding(top = 24.dp, start = 16.dp)
      )

      MediaDetailsDescriptionContent(
        description = mediaDetails.description,
        modifier = Modifier.padding(horizontal = 16.dp)
      )
    }

    mediaDetails.mediaCharacters?.let { characters ->
      ParticipantsRow(
        participants = characters,
        title = "Characters",
        modifier = Modifier.padding(top = 16.dp)
      ).takeIf { characters.isNotEmpty() }
    }

    mediaDetails.mediaStaff?.let { staff ->
      ParticipantsRow(
        participants = staff,
        title = "Staff",
        modifier = Modifier.padding(top = 16.dp)
      ).takeIf { staff.isNotEmpty() }
    }

    Spacer(
      modifier = Modifier
        .height(100.dp)
        .fillMaxWidth()
    )
  }
}



@Composable
fun ParticipantsRow(
  participants: List<MediaParticipant>,
  title: String,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
  ) {
    Row(modifier = Modifier.padding(horizontal = 16.dp )) {
      Text(text = title)
      Spacer(modifier = Modifier.weight(1f))
      Text(text = "View All")
    }
    LazyRow(
      modifier = Modifier.padding(top = 8.dp),
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(participants) { participant ->
        MediaParticipant(character = participant)
      }
    }
  }
}

@Composable
fun MediaParticipant(character: MediaParticipant) {
  val imageLoader =
    rememberAsyncImagePainter(
      model = character.imageUrl,
    )
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.height(104.dp)
  ) {
    Image(
      painter = imageLoader,
      contentDescription = character.name,
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop
    )
    Text(
      text = character.name,
      style = MaterialTheme.typography.bodySmall,
      modifier = Modifier.align(Alignment.CenterHorizontally)
    )
  }
}

@Composable
fun CloseIconButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  IconButton(
    onClick = onClick,
    modifier = modifier
      .size(32.dp)
      .clip(RoundedCornerShape(50))
      .background(Color.Black.copy(0.5f))
  ) {
    Icon(
      imageVector = Icons.Filled.ArrowBack,
      contentDescription = "Close",
      tint = Color.White,
      modifier = Modifier.size(24.dp)
    )
  }
}
