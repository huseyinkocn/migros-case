package com.example.news.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteListUiModel(
    val favoriteList: List<Article> = emptyList()
) : Parcelable
