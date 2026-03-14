package com.example.news.domain.usecase

import com.example.news.domain.model.Article
import com.example.news.domain.repository.ArticleRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(article: Article) {
        repository.toggleFavorite(article)
    }
}
