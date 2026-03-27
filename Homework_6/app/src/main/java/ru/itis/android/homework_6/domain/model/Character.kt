package ru.itis.android.homework_6.domain.model

data class Character(
    val id: Int,
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

sealed interface Result<out T> {
    data class Success<T>(val data: T, val isFromCache: Boolean = false) : Result<T>
    data class Error(val message: String) : Result<Nothing>
}