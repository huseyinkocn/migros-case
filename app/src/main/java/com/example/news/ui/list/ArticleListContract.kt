package com.example.news.ui.list

import com.example.news.R
import com.example.news.domain.model.ArticleUiModel
import com.example.news.util.Resource

object ArticleListContract {

    data class ArticleListState(
        val article: List<ArticleUiModel> = emptyList()
    )

    data object ArticleListViewState {
        val btnRetry: Int = R.string.retry
        val searchHint: Int = R.string.search_hint
    }

    sealed interface ArticleListAction {
        data class onAddFavoriteClick(val article: ArticleUiModel) : ArticleListAction
        data class onItemClick(val article: ArticleUiModel) : ArticleListAction
    }

    sealed interface ArticleListEffect {
        data class ItemClick(val article: ArticleUiModel) : ArticleListEffect
    }
}
