package com.hedmer.anibreak.data.repository

import com.hedmer.anibreak.core.database.dao.RecentSearchMediaDao
import com.hedmer.anibreak.core.database.dao.RecentSearchQueryDao
import com.hedmer.anibreak.core.database.model.common.MediaTitleEntity
import com.hedmer.anibreak.core.database.model.search.RecentSearchMediaEntity
import com.hedmer.anibreak.core.database.model.search.RecentSearchQueryEntity
import com.hedmer.anibreak.core.network.client.AnibreakGraphqlClient
import com.hedmer.anibreak.model.MediaSeason
import com.hedmer.anibreak.model.SearchBasicMedia
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Before
import org.junit.Test

class SearchDefaultRepositoryTest {

  private val apiClient: AnibreakGraphqlClient = mockk(relaxed = true)
  private val searchQueryDao: RecentSearchQueryDao = mockk(relaxed = true)
  private val searchMediaDao: RecentSearchMediaDao = mockk(relaxed = true)

  private lateinit var repository: SearchDefaultRepository

  @Before
  fun setUp() {
    repository = SearchDefaultRepository(apiClient, searchQueryDao, searchMediaDao)
  }

  @Test
  fun `upsertRecentSearchQuery inserts query correctly`() = runTest {
    val query = "Naruto"
    coEvery { searchQueryDao.upsertRecentSearchQuery(any()) } returns Unit

    repository.upsertRecentSearchQuery(query)

    coVerify { searchQueryDao.upsertRecentSearchQuery(any()) }
  }

  @Test
  fun `removeRecentQuery removes query correctly`() = runTest {
    val query = "One Piece"
    coEvery { searchQueryDao.removeRecentSearchQuery(any()) } returns Unit

    repository.removeRecentQuery(query)

    coVerify { searchQueryDao.removeRecentSearchQuery(query) }
  }

  @Test
  fun `upsertRecentSearchMedia adds media correctly`() = runTest {
    val searchMedia = SearchBasicMedia(1, null, null, null, MediaSeason.SPRING, 0)
    coEvery { searchMediaDao.upsertRecentSearchMedia(any()) } returns Unit

    repository.upsertRecentSearchMedia(searchMedia)

    coVerify { searchMediaDao.upsertRecentSearchMedia(any()) }
  }

  @Test
  fun `fetchLocalRecentSearches returns combined flow of queries and media`() = runTest {
    val searchMediaEntity = RecentSearchMediaEntity(
      1,
      MediaTitleEntity("Naruto", "Naruto", "Naruto"),
      "URL",
      MediaSeason.SPRING
    )
    every { searchQueryDao.getRecentSearchQueryEntities() } returns flowOf(
      listOf(
        RecentSearchQueryEntity("Naruto", Clock.System.now())
      )
    )
    every { searchMediaDao.getRecentSearchMediaEntities() } returns flowOf(listOf(searchMediaEntity))

    val result = repository.fetchLocalRecentSearches().first()

    assertEquals("Naruto", result?.queries?.first()?.query)
    assertEquals(searchMediaEntity.id, result?.media?.first()?.id)
  }
}
