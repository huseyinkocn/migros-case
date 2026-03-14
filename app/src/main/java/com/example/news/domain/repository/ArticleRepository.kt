package com.example.news.domain.repository

import com.example.news.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    suspend fun getArticles(limit: Int = 20, offset: Int = 0): List<Article>

    suspend fun getArticleById(id: Int): Article

    suspend fun searchArticles(query: String, limit: Int = 20, offset: Int = 0): List<Article>

    suspend fun toggleFavorite(article: Article)

    fun getFavorites(): Flow<List<Article>>

    fun isFavorite(articleId: Int): Flow<Boolean>
}
