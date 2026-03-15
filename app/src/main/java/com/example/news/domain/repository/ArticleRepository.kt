package com.example.news.domain.repository

import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    suspend fun getArticles(limit: Int = 20, offset: Int = 0): List<ArticleUiModel>

    suspend fun getArticleById(id: Int): ArticleUiModel

    suspend fun searchArticles(query: String, limit: Int = 20, offset: Int = 0): List<ArticleUiModel>

    suspend fun toggleFavorite(article: ArticleUiModel)

    fun getFavorites(): Flow<List<ArticleUiModel>>

    fun isFavorite(articleId: Int): Flow<Boolean>
}
