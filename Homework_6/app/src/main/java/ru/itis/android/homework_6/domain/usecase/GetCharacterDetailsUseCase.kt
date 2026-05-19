package ru.itis.android.homework_6.domain.usecase

import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.repository.CharacterRepository

class GetCharacterDetailsUseCase(private val repository: CharacterRepository) {
    suspend operator fun invoke(id: Int): Result<Character> {
        return repository.getCharacterById(id)
    }
}