package com.example.news.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArticleUiModel(
    val id: Int = -1,
    val title: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val newsSite: String = "",
    val summary: String = "",
    val publishedAt: String = "",
    val displayDate: String = "",
    val isFavorite: Boolean = false,
    val updatedAt: String = "",
) : Parcelable
