package com.example.news.data.repository

import com.example.news.data.local.dao.ArticleDao
import com.example.news.data.local.entity.FavoriteArticleEntity
import com.example.news.data.remote.api.SpaceflightApi
import com.example.news.data.remote.dto.ArticleDto
import com.example.news.data.remote.dto.ArticleResponseDto
import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ArticleRepositoryImplTest {

    private lateinit var api: SpaceflightApi
    private lateinit var dao: ArticleDao
    private lateinit var repository: ArticleRepositoryImpl

    private val testArticleDto = ArticleDto(
        id = 1,
        title = "Test Article",
        url = "https://example.com",
        imageUrl = "https://example.com/image.jpg",
        newsSite = "TestSite",
        summary = "Test summary",
        publishedAt = "2026-03-11T10:00:00Z",
        updatedAt = "2026-03-11T10:00:00Z",
        featured = false
    )

    private val testEntity = FavoriteArticleEntity(
        id = 1,
        title = "Test Article",
        url = "https://example.com",
        imageUrl = "https://example.com/image.jpg",
        newsSite = "TestSite",
        summary = "Test summary",
        publishedAt = "2026-03-11T10:00:00Z",
        updatedAt = "2026-03-11T10:00:00Z"
    )

    @Before
    fun setup() {
        api = mockk()
        dao = mockk(relaxed = true)
        repository = ArticleRepositoryImpl(api, dao)
    }

    @Test
    fun `getArticles returns articles from API`() = runTest {
        val response = ArticleResponseDto(
            count = 1, next = null, previous = null, results = listOf(testArticleDto)
        )
        coEvery { api.getArticles(any(), any()) } returns response
        coEvery { dao.isFavorite(any()) } returns false

        val result = repository.getArticles()

        assertEquals(1, result.size)
        assertEquals("Test Article", result[0].title)
        assertFalse(result[0].isFavorite)
    }

    @Test
    fun `getArticles marks favorite articles correctly`() = runTest {
        val response = ArticleResponseDto(
            count = 1, next = null, previous = null, results = listOf(testArticleDto)
        )
        coEvery { api.getArticles(any(), any()) } returns response
        coEvery { dao.isFavorite(1) } returns true

        val result = repository.getArticles()

        assertTrue(result[0].isFavorite)
    }

    @Test
    fun `searchArticles returns filtered results`() = runTest {
        val response = ArticleResponseDto(
            count = 1, next = null, previous = null, results = listOf(testArticleDto)
        )
        coEvery { api.searchArticles(any(), any(), any()) } returns response
        coEvery { dao.isFavorite(any()) } returns false

        val result = repository.searchArticles("test")

        assertEquals(1, result.size)
        assertEquals("Test Article", result[0].title)
    }

    @Test
    fun `toggleFavorite adds article when not favorite`() = runTest {
        coEvery { dao.isFavorite(1) } returns false

        val article = ArticleUiModel(
            id = 1, title = "Test", url = "https://example.com",
            imageUrl = "", newsSite = "", summary = "",
            publishedAt = "", updatedAt = ""
        )
        repository.toggleFavorite(article)

        coVerify { dao.insertFavorite(any()) }
    }

    @Test
    fun `toggleFavorite removes article when already favorite`() = runTest {
        coEvery { dao.isFavorite(1) } returns true

        val article = ArticleUiModel(
            id = 1, title = "Test", url = "https://example.com",
            imageUrl = "", newsSite = "", summary = "",
            publishedAt = "", updatedAt = ""
        )
        repository.toggleFavorite(article)

        coVerify { dao.deleteFavoriteById(1) }
    }

    @Test
    fun `getFavorites returns flow of favorite articles`() = runTest {
        coEvery { dao.getAllFavorites() } returns flowOf(listOf(testEntity))

        val result = repository.getFavorites().first()

        assertEquals(1, result.size)
        assertEquals("Test Article", result[0].title)
        assertTrue(result[0].isFavorite)
    }
}
