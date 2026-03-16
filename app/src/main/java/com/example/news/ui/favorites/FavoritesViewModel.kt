package com.example.news.ui.favorites

import com.example.news.domain.usecase.GetFavoritesUseCase
import com.example.news.ui.base.CoreViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : CoreViewModel() {

    private val _viewState = MutableStateFlow(FavoritesContract.FavoriteViewState)
    val viewState = _viewState.asStateFlow()

    private val _state = MutableStateFlow(FavoritesContract.FavoriteState())
    val state: StateFlow<FavoritesContract.FavoriteState> = _state.asStateFlow()

    private val _effect = Channel<FavoritesContract.FavoriteEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        launchWithLoading {
            getFavoritesUseCase().collect { favorites ->
                stopLoading()
                _state.update { it.copy(favoriteList = favorites) }
            }
        }
    }

    fun onAction(action: FavoritesContract.FavoriteAction) {
        when (action) {
            is FavoritesContract.FavoriteAction.OnItemClick -> {
                _effect.trySend(FavoritesContract.FavoriteEffect.ItemClick(action.article))
            }
        }
    }
}
