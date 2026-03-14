package com.example.news.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.news.data.local.dao.ArticleDao
import com.example.news.data.local.entity.FavoriteArticleEntity

@Database(
    entities = [FavoriteArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
