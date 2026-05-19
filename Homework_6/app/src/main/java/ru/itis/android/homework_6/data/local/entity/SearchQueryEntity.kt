package ru.itis.android.homework_6.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQueryEntity(
    @PrimaryKey val query: String,
    val timestamp: Long
)