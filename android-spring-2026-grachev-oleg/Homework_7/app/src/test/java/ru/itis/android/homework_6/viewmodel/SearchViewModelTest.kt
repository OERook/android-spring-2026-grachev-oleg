package ru.itis.android.homework_6.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.usecase.SearchCharacterUseCase
import ru.itis.android.homework_6.presentation.search.SearchUiState
import ru.itis.android.homework_6.presentation.search.SearchViewModel
import ru.itis.android.homework_6.util.UserIdProvider

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val searchUseCase: SearchCharacterUseCase = mockk()
    private val userIdProvider: UserIdProvider = mockk()
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { userIdProvider.getOrCreate() } returns "test-user-id-1234"
        savedStateHandle = SavedStateHandle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

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

    private fun createViewModel(): SearchViewModel =
        SearchViewModel(searchUseCase, userIdProvider, savedStateHandle)

    @Test
    fun `performSearch emits Success with characters list when use case returns data`() = runTest {
        val query = "Rick"
        val characters = listOf(character(1, "Rick Sanchez"))
        coEvery { searchUseCase(query) } returns Result.Success(characters, isFromCache = false)

        val viewModel = createViewModel()
        viewModel.updateQuery(query)
        viewModel.performSearch()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected Success, got $state", state is SearchUiState.Success)
        state as SearchUiState.Success
        assertEquals(characters, state.characters)
        assertEquals(false, state.isFromCache)
        assertEquals(false, state.isMessageConsumed)
    }

    @Test
    fun `performSearch resets state to Idle and does not call use case when query is blank`() = runTest {
        val viewModel = createViewModel()
        viewModel.updateQuery("   ")
        viewModel.performSearch()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is SearchUiState.Idle)
        coVerify(exactly = 0) { searchUseCase(any()) }
    }

    @Test
    fun `consumeMessage marks Success state as message-consumed`() = runTest {
        val query = "Rick"
        val characters = listOf(character(1, "Rick Sanchez"))
        coEvery { searchUseCase(query) } returns Result.Success(characters, isFromCache = true)

        val viewModel = createViewModel()
        viewModel.updateQuery(query)
        viewModel.performSearch()
        advanceUntilIdle()

        val before = viewModel.uiState.value as SearchUiState.Success
        assertEquals(false, before.isMessageConsumed)

        viewModel.consumeMessage()

        val after = viewModel.uiState.value as SearchUiState.Success
        assertEquals(true, after.isMessageConsumed)
        assertEquals(characters, after.characters)
        assertEquals(true, after.isFromCache)
    }

    @Test
    fun `performSearch emits Error state with message when use case returns Error`() = runTest {
        val query = "Unknown"
        val errorMessage = "Network unavailable"
        coEvery { searchUseCase(query) } returns Result.Error(errorMessage)

        val viewModel = createViewModel()
        viewModel.updateQuery(query)
        viewModel.performSearch()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected Error, got $state", state is SearchUiState.Error)
        assertEquals(errorMessage, (state as SearchUiState.Error).message)
    }
}
