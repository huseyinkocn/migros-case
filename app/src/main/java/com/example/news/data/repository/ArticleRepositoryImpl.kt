package com.example.news.data.repository

import com.example.news.data.local.dao.ArticleDao
import com.example.news.data.mapper.toDomain
import com.example.news.data.mapper.toEntity
import com.example.news.data.remote.api.SpaceflightApi
import com.example.news.domain.model.Article
import com.example.news.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val api: SpaceflightApi,
    private val dao: ArticleDao
) : ArticleRepository {

    override suspend fun getArticles(limit: Int, offset: Int): List<Article> {
        return api.getArticles(limit, offset).results.map { dto ->
            val isFav = dao.isFavorite(dto.id)
            dto.toDomain().copy(isFavorite = isFav)
        }
    }

    override suspend fun getArticleById(id: Int): Article {
        val dto = api.getArticleById(id)
        val isFav = dao.isFavorite(dto.id)
        return dto.toDomain().copy(isFavorite = isFav)
    }

    override suspend fun searchArticles(query: String, limit: Int, offset: Int): List<Article> {
        return api.searchArticles(query, limit, offset).results.map { dto ->
            val isFav = dao.isFavorite(dto.id)
            dto.toDomain().copy(isFavorite = isFav)
        }
    }

    override suspend fun toggleFavorite(article: Article) {
        val isFav = dao.isFavorite(article.id)
        if (isFav) {
            dao.deleteFavoriteById(article.id)
        } else {
            dao.insertFavorite(article.toEntity())
        }
    }

    override fun getFavorites(): Flow<List<Article>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isFavorite(articleId: Int): Flow<Boolean> {
        return dao.isFavoriteFlow(articleId)
    }
}
