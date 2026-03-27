package ru.itis.android.homework_6.domain.model

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episodeCode: String,
    val characterUrls: List<String>
)
