package com.example.news.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.repository.ArticleRepository
import com.example.news.domain.usecase.GetArticleDetailUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.ui.base.CoreViewModel
import com.example.news.ui.favorites.FavoritesContract
import com.example.news.ui.favorites.FavoritesContract.FavoriteEffect.*
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
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
    val state = _state.onStart {
        getArticleDetail(articleId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _state.value
    )

    private fun getArticleDetail(articleId: Int) {
        viewModelScope.launch {
            startLoading()
            getArticleDetailUseCase(articleId).collect { article ->
                stopLoading()
                _state.update { state ->
                    state.copy(article = article)
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            toggleFavoriteUseCase(state.value.article)
            _state.update { state ->
                state.copy(article = state.article.copy(isFavorite = !state.article.isFavorite))
            }
        }
    }

    fun onAction(action: ArticleDetailContract.ArticleDetailAction) {
        when (action) {
            ArticleDetailContract.ArticleDetailAction.onLoadArticle -> {
                getArticleDetail(articleId)
            }
            is ArticleDetailContract.ArticleDetailAction.onFavoriteClick -> {
                toggleFavorite()
            }
        }
    }
}
