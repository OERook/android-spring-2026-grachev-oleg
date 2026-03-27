package ru.itis.android.homework_6.domain.repository

import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result

interface CharacterRepository {
    suspend fun searchCharacters(query: String): ru.itis.android.homework_6.domain.model.Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<Character>
}
