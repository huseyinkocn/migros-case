package com.example.news.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.Article
import com.example.news.domain.repository.ArticleRepository
import com.example.news.domain.usecase.GetArticleDetailUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getArticleDetailUseCase: GetArticleDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val repository: ArticleRepository
) : ViewModel() {

    private val articleId: Int = savedStateHandle.get<Int>("articleId") ?: -1

    private val _article = MutableStateFlow<Resource<Article>>(Resource.Loading)
    val article: StateFlow<Resource<Article>> = _article.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadArticle()
        observeFavoriteStatus()
    }

    fun loadArticle() {
        getArticleDetailUseCase(articleId).onEach { result ->
            _article.value = result
        }.launchIn(viewModelScope)
    }

    private fun observeFavoriteStatus() {
        repository.isFavorite(articleId).onEach { isFav ->
            _isFavorite.value = isFav
        }.launchIn(viewModelScope)
    }

    fun toggleFavorite() {
        val currentArticle = (_article.value as? Resource.Success)?.data ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(currentArticle)
        }
    }
}
