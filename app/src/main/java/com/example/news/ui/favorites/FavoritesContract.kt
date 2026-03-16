package com.example.news.ui.favorites

import com.example.news.R
import com.example.news.domain.model.ArticleUiModel

const val ARTICLE_ID_KEY = "articleId"

object FavoritesContract {

    data class FavoriteState(
        val favoriteList: List<ArticleUiModel> = emptyList()
    )

    data object FavoriteViewState {
        val headerTitleRes: Int = R.string.menu_bookmark
    }

    sealed interface FavoriteAction {
        data class onAddFavoriteClick(val article: ArticleUiModel) : FavoriteAction
        data class onItemClick(val article: ArticleUiModel) : FavoriteAction
    }

    sealed interface FavoriteEffect {
        data class ItemClick(val article: ArticleUiModel) : FavoriteEffect
    }
}
