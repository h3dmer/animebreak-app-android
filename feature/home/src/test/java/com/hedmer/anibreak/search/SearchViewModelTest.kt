package com.hedmer.anibreak.search

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.hedmer.anibreak.domain.repository.SearchRepository
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.model.SearchBasicMedia
import com.hedmer.anibreak.search.SearchUiState.LoadFailed
import com.hedmer.anibreak.search.SearchUiState.Loading
import com.hedmer.anibreak.search.SearchUiState.Success
import com.hedmer.anibreak.testing.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private val searchRepository: SearchRepository =
    mockk(relaxed = true)

  private val stateHandle = SavedStateHandle()

  private lateinit var viewModel: SearchViewModel

  @Test
  fun `initial state as empty query`() {
    viewModel = createViewModel()

    assertThat(viewModel.uiState.value).isEqualTo(SearchUiState.EmptyQuery)
  }

  @Test
  fun `state with invalid query`() = runTest {
    viewModel = createViewModel()

    viewModel.onSearchTriggered("")

    viewModel.uiState.test {
      assertTrue(awaitItem() is SearchUiState.EmptyQuery)
    }
    coVerify(exactly = 1) {
      searchRepository.upsertRecentSearchQuery(any())
    }
  }

  @Test
  fun initialSavedStateWithEmptyQuery() {
    viewModel = createViewModel()

    assertEquals("", stateHandle["searchQuery"])
  }

  @Test
  fun onSearchTrigger_shouldChangeStateHandleValue()  {
    viewModel = createViewModel()

    viewModel.onSearchTriggered("query")

    assertEquals("query", stateHandle["searchQuery"])
  }

  @Test
  fun `valid search query triggers success state`() = runTest {
    val query = "the breaker"
    val expectedPagingData = PagingData.empty<SearchBasicMedia>()
    every { searchRepository.searchMedia(1, query) } returns flowOf(expectedPagingData)
    viewModel = createViewModel()

    viewModel.onSearchTriggered(query)
    val collectSearchResults = launch(UnconfinedTestDispatcher()) {
      viewModel.searchResults.collect()
    }
    val result = viewModel.uiState.value

    assertThat(result).isInstanceOf(Success::class.java)
    collectSearchResults.cancel()
  }

  @Test
  fun successSearchWithValidQuery() = runTest {
    val query = "the breaker"
    val expectedPagingData = PagingData.empty<SearchBasicMedia>()
    val observedStates = mutableListOf<SearchUiState>()
    every { searchRepository.searchMedia(1, query) } returns flowOf(expectedPagingData)

    viewModel = createViewModel()

    val collectSearchResults = backgroundScope.launch(UnconfinedTestDispatcher()) {
      viewModel.searchResults.collect()
    }

    val collectUiState = launch(UnconfinedTestDispatcher()) {
      viewModel.uiState.toList(observedStates)
    }
    viewModel.onSearchTriggered(query)

    assertThat(observedStates).containsExactly(
      SearchUiState.EmptyQuery,
      Loading,
      Success()
    ).inOrder()

    collectUiState.cancel()
    collectSearchResults.cancel()
  }

  @Test
  fun `given invalid query then set empty query state`() = runTest {
    val query = ""
    val expectedPagingData = PagingData.empty<SearchBasicMedia>()
    val observedStates = mutableListOf<SearchUiState>()
    every { searchRepository.searchMedia(1, query) } returns flowOf(expectedPagingData)

    viewModel = createViewModel()

    val searchResultsJob = backgroundScope.launch(UnconfinedTestDispatcher()) {
      viewModel.searchResults.collect()
    }

    val collectUiStateJob = launch(UnconfinedTestDispatcher()) {
      viewModel.uiState.toList(observedStates)
    }

    viewModel.onSearchTriggered(query)

    assertThat(observedStates).containsNoneOf(Loading, Success(), LoadFailed)

    collectUiStateJob.cancel()
    searchResultsJob.cancel()
  }

  @Test
  fun `given ten results then update items count`() = runTest {
    val expectedCounts = 10
    coEvery { searchRepository.totalCountsFlow } returns MutableStateFlow(expectedCounts)

    viewModel = createViewModel()

    viewModel.totalItemsCount.test {
      assertThat(expectedCounts).isEqualTo(awaitItem())
    }
  }

  @Test
  fun `on media search click then upsert media`() = runTest {
    val expectedMedia = SearchBasicMedia(0, null, null, null, MediaSeason.FALL, 0)
    viewModel = createViewModel()

    viewModel.onMediaSearchClick(expectedMedia)

    coVerify {
      searchRepository.upsertRecentSearchMedia(expectedMedia)
    }
  }

  @Test
  fun `on search query remove icon click then trigger onRemoveSearch()`() = runTest {
    val expected = "ab"
    viewModel = createViewModel()

    viewModel.onRemoveSearch(expected)

    coVerify {
      searchRepository.removeRecentQuery(expected)
    }
  }

  @Test
  fun `on empty search results then return null`() = runTest {
    viewModel = createViewModel()

    viewModel.recentSearchers.test {
      assertThat(awaitItem()).isEqualTo(null)
    }
  }

  private fun createViewModel() =
    SearchViewModel(
      stateHandle,
      searchRepository
    )
}
