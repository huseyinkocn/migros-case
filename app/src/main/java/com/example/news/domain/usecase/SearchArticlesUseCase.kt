package com.example.news.domain.usecase

import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import com.example.news.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(query: String, limit: Int = 20, offset: Int = 0): Flow<List<ArticleUiModel>> = flow {
        try {
            val articles = repository.searchArticles(query, limit, offset)
            emit(articles)
        } catch (e: Exception) {
                //
        }
    }
}
