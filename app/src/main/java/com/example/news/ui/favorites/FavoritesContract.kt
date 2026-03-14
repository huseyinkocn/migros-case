package com.example.news.ui.favorites

import com.example.news.R
import com.example.news.domain.model.Article
import com.example.news.domain.model.FavoriteListUiModel

const val ARTICLE_ID_KEY = "articleId"

object FavoritesContract {

    data class FavoriteState(
        val favoriteListUiModel: FavoriteListUiModel = FavoriteListUiModel(),
        val favoriteList: List<Article> = emptyList()
    )

    data object FavoriteViewState{
        val headerTitleRes: Int = R.string.menu_bookmark
    }

    sealed interface FavoriteAction {
        data class onAddFavoriteClick(val article: Article) : FavoriteAction
        data class onItemClick(val article: Article) : FavoriteAction
    }

    sealed interface FavoriteEffect {
        data class ItemClick(val article: Article) : FavoriteEffect
    }
}
