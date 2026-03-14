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

inline fun <T> RestResult<T>.onSuccess(action: (T) -> Unit): RestResult<T> {
    if (this is RestResult.Success) action(result)
    return this
}

inline fun <T> RestResult<T>.onSuccessWithStatus(
    action: (T, IResponseStatus) -> Unit
): RestResult<T> {
    if (this is RestResult.SuccessWithStatus) action(result, status)
    return this
}

inline fun <T> RestResult<T>.onEmpty(action: (isSucceeded: Boolean) -> Unit): RestResult<T> {
    if (this is RestResult.Empty) action(isSucceeded)
    return this
}

inline fun <T> RestResult<T>.onError(action: (INetworkError) -> Unit): RestResult<T> {
    if (this is RestResult.Error) action(error)
    return this
}

inline fun <T> RestResult<T>.onLoading(action: (loading: Boolean) -> Unit): RestResult<T> {
    if (this is RestResult.Loading) action(loading)
    return this
}

inline fun <T> RestResult<T>.orElse(action: () -> Unit): RestResult<T> {
    if (this !is RestResult.Success) action()
    return this
}

inline fun <T, R> RestResult<T>.mapOnSuccess(map: (T?) -> R): RestResult<R> = when (this) {
    is RestResult.Success -> RestResult.Success(map(result))
    is RestResult.SuccessWithStatus -> RestResult.SuccessWithStatus(map(result), status)
    is RestResult.Empty -> this
    is RestResult.Error -> this
    is RestResult.Loading -> this
}

interface OnSuccess<T> {
    fun invoke(data: T)
}

interface OnFailure<T> {
    fun invoke(result: T?, exception: NewsNetworkException)
}

interface OnError {
    fun invoke(exception: INetworkError)
}

interface OnLoading {
    fun invoke()
}
