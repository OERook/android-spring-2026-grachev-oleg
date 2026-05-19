package ru.itis.android.homework_6.domain.usecase

import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.repository.CharacterRepository

class SearchCharacterUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(query: String): Result<List<Character>> {
        if (query.isBlank()) return Result.Success(emptyList(), isFromCache = true)
        return repository.searchCharacters(query)
    }
}