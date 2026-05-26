package ru.itis.android.homework_6.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.repository.CharacterRepository
import ru.itis.android.homework_6.domain.usecase.GetCharacterDetailsUseCase

class GetCharacterDetailsUseCaseTest {

    private val repository: CharacterRepository = mockk()
    private val useCase = GetCharacterDetailsUseCase(repository)

    @Test
    fun `invoke calls repository once and returns the character for given id`() = runTest {
        val id = 42
        val expected = Character(
            id = id,
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            locationName = "Citadel of Ricks",
            imageUrl = "https://example.com/$id.png",
            episodeUrls = persistentListOf("https://example.com/ep/1")
        )
        coEvery { repository.getCharacterById(id) } returns Result.Success(expected)

        val result = useCase(id)

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
        coVerify(exactly = 1) { repository.getCharacterById(id) }
    }
}
