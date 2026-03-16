package com.example.news.domain.network

sealed class RestResult<out T> {

    class Success<out T>(
        val result: T
    ) : RestResult<T>()

    class SuccessWithStatus<out T>(
        val result: T,
        val status: IResponseStatus
    ) : RestResult<T>()

    class Empty(val isSucceeded: Boolean) : RestResult<Nothing>()

    class Error(
        val error: INetworkError
    ) : RestResult<Nothing>()

    class Loading(val loading: Boolean) : RestResult<Nothing>()
}
