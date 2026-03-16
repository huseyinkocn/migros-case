package com.example.news.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.news.domain.usecase.GetArticleDetailUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.ui.base.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : CoreViewModel() {

    private val articleId: Int = savedStateHandle.get<Int>(ARTICLE_ID_KEY) ?: -1

    private val _viewState = MutableStateFlow(ArticleDetailContract.ArticleDetailViewState)
    val viewState = _viewState.asStateFlow()

    private val _state = MutableStateFlow(ArticleDetailContract.ArticleDetailState())
    val state: StateFlow<ArticleDetailContract.ArticleDetailState> = _state.asStateFlow()

    init {
        getArticleDetail(articleId)
    }

    private fun getArticleDetail(articleId: Int) {
        launchWithLoading {
            val article = getArticleDetailUseCase(articleId)
            _state.update { it.copy(article = article) }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(state.value.article)
            _state.update { it.copy(article = it.article.copy(isFavorite = !it.article.isFavorite)) }
        }
    }

    fun onAction(action: ArticleDetailContract.ArticleDetailAction) {
        when (action) {
            ArticleDetailContract.ArticleDetailAction.OnLoadArticle -> {
                getArticleDetail(articleId)
            }
            is ArticleDetailContract.ArticleDetailAction.OnFavoriteClick -> {
                toggleFavorite()
            }
        }
    }
}
