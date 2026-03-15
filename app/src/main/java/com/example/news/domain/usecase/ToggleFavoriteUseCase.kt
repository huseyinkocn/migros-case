package com.example.news.domain.usecase

import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(article: ArticleUiModel) {
        repository.toggleFavorite(article)
    }
}
