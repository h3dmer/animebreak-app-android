package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hedmer.anibreak.model.search.FilterState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFiltersBottomSheet(
  filter: FilterState,
  onDismiss: () -> Unit
) {
  val modalBottomSheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = { onDismiss() },
    sheetState = modalBottomSheetState,
    dragHandle = { BottomSheetDefaults.DragHandle() },
  ) {
    FilterList(filter, onDismiss)
  }
}

@Composable
fun FilterList(
  filter: FilterState,
  onDismiss: () -> Unit
) {
  LazyColumn {
    items(filter.dataTypes) {filterDataType ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 10.dp, horizontal = 20.dp)
          .clickable {
            filter.onFilterChange(filter, filterDataType.filterName)
            onDismiss()
          }
      ) {
        Text(
          text = filterDataType.filterName,
          modifier = Modifier.padding(end = 20.dp)
        )
      }
    }
  }
}