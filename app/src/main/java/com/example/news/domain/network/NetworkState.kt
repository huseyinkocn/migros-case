package com.example.news.domain.network

sealed interface NetworkState {

    data class Error(val error: INetworkError?) : NetworkState

    data class Success(val args: Any, val status: IResponseStatus? = null) : NetworkState
}

data class NetworkStateLoadingState(
    val loadingCount: Int = 0,
    val isLoading: Boolean = false
)
