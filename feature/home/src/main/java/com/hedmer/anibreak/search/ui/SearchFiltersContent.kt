package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hedmer.anibreak.model.search.FilterState

@Composable
internal fun SearchFiltersContent(
  filters: List<FilterState>
) {

  var showSheet by remember {
    mutableStateOf(
      Pair<FilterState?, Boolean>(null, false)
    )
  }

  if (showSheet.second) {
    showSheet.first?.let {
      SearchFiltersBottomSheet(it) {
        showSheet = null to false
      }
    }
  }

  Row(
    modifier = Modifier
      .horizontalScroll(rememberScrollState())
      .padding(8.dp)
  ) {
    filters.forEach {
      FilterChip(it) { filterResource ->
        showSheet = filterResource to true
      }
    }
  }
}
