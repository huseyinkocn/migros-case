package com.example.news.data.mapper

import com.example.news.data.local.entity.FavoriteArticleEntity
import com.example.news.data.remote.dto.ArticleDto
import com.example.news.domain.model.Article

fun ArticleDto.toDomain(): Article {
    return Article(
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

fun FavoriteArticleEntity.toDomain(): Article {
    return Article(
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

fun Article.toEntity(): FavoriteArticleEntity {
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
