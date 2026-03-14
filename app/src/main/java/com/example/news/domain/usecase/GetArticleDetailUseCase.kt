package com.example.news.domain.usecase

import com.example.news.domain.model.Article
import com.example.news.domain.repository.ArticleRepository
import com.example.news.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticleDetailUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(articleId: Int): Flow<Resource<Article>> = flow {
        emit(Resource.Loading)
        try {
            val article = repository.getArticleById(articleId)
            emit(Resource.Success(article))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
