package com.example.news.domain.usecase

import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import javax.inject.Inject

class SearchArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(query: String, limit: Int = 20, offset: Int = 0): List<ArticleUiModel> {
        return repository.searchArticles(query, limit, offset)
    }
}
