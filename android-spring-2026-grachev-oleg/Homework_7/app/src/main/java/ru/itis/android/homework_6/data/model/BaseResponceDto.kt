package ru.itis.android.homework_6.data.model

import com.google.gson.annotations.SerializedName

data class PagedResponseDto<T>(
    @SerializedName("info")
    val info: InfoDto,
    @SerializedName("results")
    val results: List<T>
)

data class InfoDto(
    @SerializedName("count")
    val count : Int,
    @SerializedName("pages")
    val pages : Int,
    @SerializedName("next")
    val next : String?,
    @SerializedName("prev")
    val prev : String?
)
