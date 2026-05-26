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
import ru.itis.android.homework_6.domain.usecase.SearchCharacterUseCase

class SearchCharacterUseCaseTest {

    private val repository: CharacterRepository = mockk()
    private val useCase = SearchCharacterUseCase(repository)

    private fun character(id: Int, name: String) = Character(
        id = id,
        name = name,
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        originName = "Earth",
        locationName = "Earth",
        imageUrl = "https://example.com/$id.png",
        episodeUrls = persistentListOf()
    )

    @Test
    fun `invoke delegates to repository and returns its result for non-blank query`() = runTest {
        val query = "Rick"
        val expected = listOf(character(1, "Rick Sanchez"), character(2, "Rick Prime"))
        coEvery { repository.searchCharacters(query) } returns Result.Success(expected, isFromCache = false)

        val result = useCase(query)

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
        coVerify(exactly = 1) { repository.searchCharacters(query) }
    }

    @Test
    fun `invoke returns empty list without touching repository when query is blank`() = runTest {
        val result = useCase("   ")

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
        coVerify(exactly = 0) { repository.searchCharacters(any()) }
    }
}
