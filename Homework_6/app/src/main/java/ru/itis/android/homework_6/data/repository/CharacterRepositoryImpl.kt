package ru.itis.android.homework_6.data.repository

import ru.itis.android.homework_6.data.api.RickAndMortyApi
import ru.itis.android.homework_6.data.local.dao.CharacterDao
import ru.itis.android.homework_6.data.local.entity.SearchQueryEntity
import ru.itis.android.homework_6.data.local.entity.toDomain
import ru.itis.android.homework_6.data.local.entity.toEntity
import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.repository.CharacterRepository
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.model.toDomain
import kotlin.collections.emptyList

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val dao : CharacterDao
) : CharacterRepository {

    private val CACHE_LIFETIME_MS = 15_000L

    override suspend fun searchCharacters(query: String): Result<List<Character>> {
        val currentTime = System.currentTimeMillis()
        val cachedQuery = dao.getSearchQuery(query)

        if (cachedQuery != null && (currentTime - cachedQuery.timestamp) < CACHE_LIFETIME_MS){
            val cachedCharacters = dao.getCharactersByQuery(query).map { it.toDomain() }
            if (cachedCharacters.isNotEmpty()) {
                return Result.Success(cachedCharacters, isFromCache = true)
            }
        }

        return try {
            val response = api.searchCharacters(query)
            if (response.isSuccessful){
                val domainCharacters = response.body()?.results?.map { it.toDomain() } ?: emptyList()

                val entities = domainCharacters.map { it.toEntity(query) }
                dao.insertSearchQuery(SearchQueryEntity(query, currentTime))
                dao.insertCharacters(entities)

                Result.Success(domainCharacters, isFromCache = false)
            } else {
                if (response.code() == 404) {
                    Result.Success(emptyList(), isFromCache = false)
                } else {
                    Result.Error("Ошибка сервера: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Result.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }

    override suspend fun getCharacterById(id: Int): Result<Character> {

        val cachedCharacter = dao.getCharacterById(id)
        if (cachedCharacter != null) {
            return Result.Success(cachedCharacter.toDomain(), isFromCache = true)
        }

        return try {
            val response = api.getCharacterById(id)
            if (response.isSuccessful && response.body() != null) {
                val domainCharacter = response.body()!!.toDomain()

                val entity = domainCharacter.toEntity("details_cache")
                dao.insertCharacters(listOf(entity))

                Result.Success(domainCharacter, isFromCache = false)
            } else {
                Result.Error("Персонаж не найден")
            }
        } catch (e : Exception) {
            Result.Error("Ошибка сети: ${e.localizedMessage}")
        }
    }
}