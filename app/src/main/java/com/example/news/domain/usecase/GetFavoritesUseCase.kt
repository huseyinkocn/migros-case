package com.example.news.domain.usecase

import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<List<ArticleUiModel>> {
        return repository.getFavorites()
    }
}
