package com.example.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.news.data.local.entity.FavoriteArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM favorite_articles ORDER BY publishedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE id = :articleId)")
    suspend fun isFavorite(articleId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(article: FavoriteArticleEntity)

    @Query("DELETE FROM favorite_articles WHERE id = :articleId")
    suspend fun deleteFavoriteById(articleId: Int)
}
