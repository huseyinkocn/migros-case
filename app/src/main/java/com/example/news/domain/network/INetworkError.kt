package com.example.news.domain.network

import com.google.gson.JsonElement

interface INetworkError {
    var message: String?
    var data: JsonElement?
    var code: Int?
    var externalCode: String?
}
