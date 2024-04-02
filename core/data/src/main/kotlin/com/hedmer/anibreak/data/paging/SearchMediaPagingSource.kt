package com.hedmer.anibreak.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hedmer.anibreak.core.network.client.AnibreakGraphqlClient
import com.hedmer.anibreak.data.mappers.toRequestParam
import com.hedmer.anibreak.model.AnibreakMediaType
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.model.MediaTitle
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.model.search.SearchParam
import query.SearchMediaQuery

private const val STARTING_PAGE_INDEX = 1
private const val ITEMS_PER_PAGE = 30

class SearchMediaPagingSource(
  private val client: AnibreakGraphqlClient,
  private val searchParam: SearchParam,
  private val totalCounts: (Int?) -> Unit
) : PagingSource<Int, SearchMediaQuery.Medium>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMediaQuery.Medium> {
    val pageNumber = params.key ?: STARTING_PAGE_INDEX
    return try {
      val response = client.searchMedia(searchParam.toRequestParam())
      val page = response.data?.Page?.pageInfo
      response.data?.Page?.media?.filterNotNull()?.let { mediaList ->
        totalCounts(page?.total)
        LoadResult.Page(
          data = mediaList,
          prevKey = if (pageNumber == 1) null else pageNumber - 1,
          nextKey = getNextKey(pageNumber, page?.lastPage)
        )
      } ?: LoadResult.Error(Throwable())

    } catch (e: Exception) {
      LoadResult.Error(e)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, SearchMediaQuery.Medium>): Int? {
    return state.anchorPosition?.let { position ->
      val anchorPage = state.closestPageToPosition(position)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

  private fun getNextKey(
    currentPageNumber: Int,
    lastPage: Int?
  ): Int? =
    lastPage?.let {
      (currentPageNumber + 1).takeIf { currentPageNumber < lastPage }
    }
}

fun createSearchMediaList(): List<SearchBasicMedia> {
  return List(50) {
    SearchBasicMedia(
      id = it,
      type = AnibreakMediaType.ANIME,
      title = MediaTitle(
        "NARUTO",
        "Naruto",
        "NARUTO -ナルト-"
      ),
      coverImage = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx20-YJvLbgJQPCoI.jpg",
      season = MediaSeason.FALL,
      rating = 79
    )
  }
}
