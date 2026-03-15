package com.example.news.ui.list

import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.usecase.GetArticlesUseCase
import com.example.news.domain.usecase.SearchArticlesUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.ui.base.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : CoreViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    private val _effect = Channel<ArticleListContract.ArticleListEffect>()
    val effect = _effect.receiveAsFlow()

    private val _viewState = MutableStateFlow(ArticleListContract.ArticleListViewState)
    val viewState = _viewState.asStateFlow()

    private val _state = MutableStateFlow(ArticleListContract.ArticleListState())
    val state = _state.onStart {
        loadArticles()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _state.value
    )

    fun loadArticles() {
        val query = _searchQuery.value
        if (query.isBlank()) {
            getArticlesUseCase().onEach { result ->
                _state.update { state ->
                    state.copy(article = result)
                }
            }.launchIn(viewModelScope)
        } else {
            searchArticles(query)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.isBlank()) {
                loadArticles()
            } else {
                searchArticles(query)
            }
        }
    }

    private fun searchArticles(query: String) {
        searchArticlesUseCase(query).onEach { result ->
            _state.update { state ->
                state.copy(article = result)
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ArticleListContract.ArticleListAction) {
        when (action) {
            is ArticleListContract.ArticleListAction.onAddFavoriteClick -> {
                toggleFavorite(action.article)
            }

            is ArticleListContract.ArticleListAction.onItemClick -> {
                _effect.trySend(ArticleListContract.ArticleListEffect.ItemClick(action.article))
            }
        }
    }

    fun toggleFavorite(article: ArticleUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(article)
            loadArticles()
        }
    }
}
