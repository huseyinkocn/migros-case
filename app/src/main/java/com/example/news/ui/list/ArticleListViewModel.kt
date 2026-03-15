package com.example.news.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.usecase.GetArticlesUseCase
import com.example.news.domain.usecase.SearchArticlesUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.ui.base.CoreViewModel
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : CoreViewModel() {

    private val _articles = MutableStateFlow<Resource<List<ArticleUiModel>>>(Resource.Loading)
    val articles: StateFlow<Resource<List<ArticleUiModel>>> = _articles.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadArticles()
    }

    fun loadArticles() {
        val query = _searchQuery.value
        if (query.isBlank()) {
            getArticlesUseCase().onEach { result ->
                _articles.value = result
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
            _articles.value = result
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite(article: ArticleUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(article)
            loadArticles()
        }
    }
}
