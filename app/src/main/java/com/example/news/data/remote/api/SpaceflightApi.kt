package com.example.news.data.remote.api

import com.example.news.data.remote.dto.ArticleResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpaceflightApi {

    @GET("articles/")
    suspend fun getArticles(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArticleResponseDto

    @GET("articles/{id}/")
    suspend fun getArticleById(
        @Path("id") id: Int
    ): com.example.news.data.remote.dto.ArticleDto

    @GET("articles/")
    suspend fun searchArticles(
        @Query("search") query: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArticleResponseDto

    companion object {
        const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"
    }
}
