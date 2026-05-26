package ru.itis.android.homework_6.data.model

import com.google.gson.annotations.SerializedName
import ru.itis.android.homework_6.domain.model.Character

data class CharacterDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val origin : LocationReferenceDto,
    @SerializedName("location")
    val location : LocationReferenceDto,
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode : List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("created")
    val created: String,
)

data class LocationReferenceDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
)


data class CharacterResponseDto(
    val results: List<CharacterDto>,
)
