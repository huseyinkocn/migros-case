package com.example.news.ui.list

import app.cash.turbine.test
import com.example.news.domain.model.Article
import com.example.news.domain.usecase.GetArticlesUseCase
import com.example.news.domain.usecase.SearchArticlesUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.util.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleListViewModelTest {

    private lateinit var getArticlesUseCase: GetArticlesUseCase
    private lateinit var searchArticlesUseCase: SearchArticlesUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: ArticleListViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testArticles = listOf(
        Article(
            id = 1, title = "Article 1", url = "https://example.com/1",
            imageUrl = "", newsSite = "TestSite", summary = "Summary 1",
            publishedAt = "2026-03-11T10:00:00Z", updatedAt = "2026-03-11T10:00:00Z"
        ),
        Article(
            id = 2, title = "Article 2", url = "https://example.com/2",
            imageUrl = "", newsSite = "TestSite", summary = "Summary 2",
            publishedAt = "2026-03-11T11:00:00Z", updatedAt = "2026-03-11T11:00:00Z"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getArticlesUseCase = mockk()
        searchArticlesUseCase = mockk()
        toggleFavoriteUseCase = mockk()

        every { getArticlesUseCase(any(), any()) } returns flowOf(Resource.Success(testArticles))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads articles successfully`() = runTest {
        viewModel = ArticleListViewModel(getArticlesUseCase, searchArticlesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.articles.test {
            val result = awaitItem()
            assertTrue(result is Resource.Success)
            assertEquals(2, (result as Resource.Success).data.size)
        }
    }

    @Test
    fun `toggleFavorite calls use case`() = runTest {
        coEvery { toggleFavoriteUseCase(any()) } returns Unit
        every { getArticlesUseCase(any(), any()) } returns flowOf(Resource.Success(testArticles))

        viewModel = ArticleListViewModel(getArticlesUseCase, searchArticlesUseCase, toggleFavoriteUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.toggleFavorite(testArticles[0])
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { toggleFavoriteUseCase(testArticles[0]) }
    }
}
