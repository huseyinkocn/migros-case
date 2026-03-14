package com.example.news.domain.usecase

import com.example.news.domain.model.Article
import com.example.news.domain.repository.ArticleRepository
import com.example.news.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(limit: Int = 20, offset: Int = 0): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading)
        try {
            val articles = repository.getArticles(limit, offset)
            emit(Resource.Success(articles))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}
