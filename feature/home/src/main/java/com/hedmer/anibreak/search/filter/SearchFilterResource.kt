package com.hedmer.anibreak.search.filter

import android.content.Context
import com.hedmer.anibreak.feature.home.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchFilterResource @Inject constructor(
  @ApplicationContext private val context: Context
) {

  fun mediaTypeHint() = context.getString(R.string.search_filter_media_type)

  fun mediaSeasonHint() = context.getString(R.string.search_filter_media_season)

  fun mediaFormatHint() = context.getString(R.string.search_filter_media_format)

  fun mediaYearHint() = context.getString(R.string.search_filter_media_year)
}
