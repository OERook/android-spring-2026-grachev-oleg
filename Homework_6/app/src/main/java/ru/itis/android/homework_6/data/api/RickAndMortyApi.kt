package ru.itis.android.homework_6.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.android.homework_6.data.model.CharacterDto
import ru.itis.android.homework_6.data.model.EpisodeDto
import ru.itis.android.homework_6.data.model.LocationDto
import ru.itis.android.homework_6.data.model.PagedResponseDto

interface RickAndMortyApi {
    @GET("api/character/")
    suspend fun searchCharacters(@Query("name") name: String) : Response<PagedResponseDto<CharacterDto>>

    @GET("api/character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int) : Response<CharacterDto>

    @GET("api/location/{id}")
    suspend fun getLocationById(@Path("id") id: Int) : Response<LocationDto>

    @GET("api/episode/{id}")
    suspend fun getEpisodeById(@Path("id") id: Int) : Response<EpisodeDto>
}