package com.example.news.data.mapper

import com.example.news.data.local.entity.FavoriteArticleEntity
import com.example.news.data.remote.dto.ArticleDto
import com.example.news.domain.model.Article
import com.example.news.domain.model.ArticleUiModel

fun ArticleDto.toDomain(): ArticleUiModel {
    return ArticleUiModel(
        id = id,
        title = title,
        url = url,
        imageUrl = imageUrl,
        newsSite = newsSite,
        summary = summary,
        publishedAt = publishedAt,
        updatedAt = updatedAt
    )
}

fun FavoriteArticleEntity.toDomain(): ArticleUiModel {
    return ArticleUiModel(
        id = id,
        title = title,
        url = url,
        imageUrl = imageUrl,
        newsSite = newsSite,
        summary = summary,
        publishedAt = publishedAt,
        updatedAt = updatedAt,
        isFavorite = true
    )
}

fun ArticleUiModel.toEntity(): FavoriteArticleEntity {
    return FavoriteArticleEntity(
        id = id,
        title = title,
        url = url,
        imageUrl = imageUrl,
        newsSite = newsSite,
        summary = summary,
        publishedAt = publishedAt,
        updatedAt = updatedAt
    )
}
