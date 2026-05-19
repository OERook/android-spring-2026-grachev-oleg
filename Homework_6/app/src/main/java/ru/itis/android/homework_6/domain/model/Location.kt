package ru.itis.android.homework_6.domain.model

data class Location(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residentUrls: List<String>
)
