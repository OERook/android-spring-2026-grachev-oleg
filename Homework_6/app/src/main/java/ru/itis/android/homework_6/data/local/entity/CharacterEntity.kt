package ru.itis.android.homework_6.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val query: String,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val imageUrl: String,
    val episodeUrls: List<String>
)
