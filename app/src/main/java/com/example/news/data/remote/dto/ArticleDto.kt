package com.example.news.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<ArticleDto>
)

data class ArticleDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("news_site") val newsSite: String,
    @SerializedName("summary") val summary: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("featured") val featured: Boolean
)
