package com.example.news.domain.model

sealed class ArticleListItem {
    data class SectionHeader(val title: String) : ArticleListItem()
    data class FeaturedArticle(val article: Article) : ArticleListItem()
    data class SmallArticle(val article: Article) : ArticleListItem()
}