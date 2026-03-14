package com.example.news.domain.network

enum class NetworkErrorType(val value: Int) {
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    TOO_MANY_REQUESTS(429),
    REQUEST_FAILED(-1),
    OTHER(-2),
    NO_INTERNET_CONNECTION(-1000),
    SECURITY_ERROR(1225)
}
