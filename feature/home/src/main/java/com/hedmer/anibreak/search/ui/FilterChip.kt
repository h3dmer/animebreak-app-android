package com.hedmer.anibreak.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hedmer.anibreak.model.search.FilterState

@Composable
fun FilterChip(
  filter: FilterState,
  onFilterSelected: (FilterState) -> Unit
) {
  val isSelected = filter.chosenFilterName != null

  AssistChip(
    onClick = { onFilterSelected(filter) },
    label = { Text(filter.chosenFilterName ?: filter.filterHint) },
    shape = CircleShape,
    trailingIcon = {
      Icon(
        modifier = Modifier.clickable {
          if (isSelected) {
            filter.onClearFilter(filter)
          } else {
            onFilterSelected(filter)
          }
        },
        imageVector = if (isSelected) Icons.Default.Close else Icons.Default.ArrowDropDown,
        contentDescription = if (isSelected) "Select" else "Deselect"
      )
    })
  Spacer(modifier = Modifier.width(8.dp))
}
