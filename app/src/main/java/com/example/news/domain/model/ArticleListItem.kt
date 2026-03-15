package com.example.news.domain.model

sealed class ArticleListItem {
    data class SectionHeader(val title: String) : ArticleListItem()
    data class FeaturedArticle(val article: ArticleUiModel) : ArticleListItem()
    data class SmallArticle(val article: ArticleUiModel) : ArticleListItem()
}
