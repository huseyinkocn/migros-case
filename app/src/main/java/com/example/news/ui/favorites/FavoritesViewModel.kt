package com.example.news.ui.favorites

import androidx.lifecycle.viewModelScope
import com.example.news.domain.model.ArticleUiModel
import com.example.news.domain.usecase.GetFavoritesUseCase
import com.example.news.domain.usecase.ToggleFavoriteUseCase
import com.example.news.ui.base.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : CoreViewModel() {

    private val _viewState = MutableStateFlow(FavoritesContract.FavoriteViewState)
    val viewState = _viewState.asStateFlow()

    private val _state = MutableStateFlow(FavoritesContract.FavoriteState())
    val state = _state.onStart {
        getNotifications()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _state.value
    )

    private val _effect = Channel<FavoritesContract.FavoriteEffect>()
    val effect = _effect.receiveAsFlow()

    fun toggleFavorite(article: ArticleUiModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(article)
        }
    }

    fun onAction(action: FavoritesContract.FavoriteAction) {
        when (action) {
            is FavoritesContract.FavoriteAction.onAddFavoriteClick -> {
                toggleFavorite(action.article)
            }

            is FavoritesContract.FavoriteAction.onItemClick -> {
                _effect.trySend(FavoritesContract.FavoriteEffect.ItemClick(action.article))
            }
        }
    }

    private fun getNotifications() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _state.update { state ->
                    state.copy(favoriteList = favorites)
                }
            }
        }
    }
}
