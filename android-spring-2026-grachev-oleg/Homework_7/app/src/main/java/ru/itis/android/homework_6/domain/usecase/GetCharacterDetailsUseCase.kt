package ru.itis.android.homework_6.domain.usecase

import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(private val repository: CharacterRepository) {
    suspend operator fun invoke(id: Int): Result<Character> {
        return repository.getCharacterById(id)
    }
}