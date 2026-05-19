package ru.itis.android.homework_6.di

import android.content.Context
import android.icu.util.TimeUnit
import androidx.room.Room
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.android.homework_6.data.api.RickAndMortyApi
import ru.itis.android.homework_6.data.local.AppDatabase
import ru.itis.android.homework_6.data.repository.CharacterRepositoryImpl
import ru.itis.android.homework_6.domain.usecase.GetCharacterDetailsUseCase
import ru.itis.android.homework_6.domain.usecase.SearchCharacterUseCase

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "rick_and_morty_db"
    ).build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(okhttp3.logging.HttpLoggingInterceptor().apply {
            level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val api = retrofit.create(RickAndMortyApi::class.java)

    private val repository = CharacterRepositoryImpl(api, database.characterDao())

    val searchCharacterUseCase = SearchCharacterUseCase(repository)
    val getCharacterDetailsUseCase = GetCharacterDetailsUseCase(repository)
}