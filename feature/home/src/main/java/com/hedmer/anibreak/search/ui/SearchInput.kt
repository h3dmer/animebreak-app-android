package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchInput(
  modifier: Modifier = Modifier,
  searchQuery: String,
  hint: String = "Search: anime/manga",
  isClearVisible: Boolean,
  onSearchQueryChanged: (String) -> Unit,
  onSearchTriggered: (String) -> Unit,
  onBackClick: () -> Unit,
  clearContent: () -> Unit
) {
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  val onSearchImeClick = {
    keyboardController?.hide()
    onSearchTriggered(searchQuery)
  }

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant)
  ) {
    TextField(
      value = searchQuery,
      onValueChange = { newValue ->
        onSearchQueryChanged(newValue)
      },
      modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp)
        .align(Alignment.CenterStart)
        .focusRequester(focusRequester)
        .onKeyEvent {
          if (it.key == Key.Enter) {
            onSearchTriggered(searchQuery)
            true
          } else {
            false
          }
        },
      placeholder = { Text(text = hint, color = MaterialTheme.colorScheme.onSurfaceVariant) },
      maxLines = 1,
      singleLine = true,
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
              onSearchQueryChanged("")
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
