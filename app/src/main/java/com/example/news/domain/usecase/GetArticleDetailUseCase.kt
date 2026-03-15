package com.example.news.domain.usecase

import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticleDetailUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(articleId: Int): Flow<ArticleUiModel> = flow {
        try {
            val article = repository.getArticleById(articleId)
            emit(article)
        } catch (e: Exception) {
            //
        }
    }
}
