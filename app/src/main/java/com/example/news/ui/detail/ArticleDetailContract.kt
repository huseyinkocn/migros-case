package com.example.news.ui.detail

import com.example.news.R
import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel

const val ARTICLE_ID_KEY = "articleId"

object ArticleDetailContract {

    data class ArticleDetailState(
        val article: ArticleUiModel = ArticleUiModel(),
    )

    data object ArticleDetailViewState {
        val following: Int = R.string.following
        val btnRetry: Int = R.string.retry
    }

    sealed interface ArticleDetailAction {
        data object onLoadArticle : ArticleDetailAction
        data object onFavoriteClick : ArticleDetailAction
    }

    sealed interface ArticleDetailEffect {
        data class ItemClick(val article: Article) : ArticleDetailEffect
    }
}
