package com.example.news.domain.network

data class NewsNetworkException(val networkError: INetworkError) : Exception()
