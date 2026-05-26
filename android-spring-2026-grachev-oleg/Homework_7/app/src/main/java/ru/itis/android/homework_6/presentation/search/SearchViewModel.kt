package ru.itis.android.homework_6.presentation.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.usecase.SearchCharacterUseCase
import ru.itis.android.homework_6.util.UserIdProvider
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchCharactersUseCase: SearchCharacterUseCase,
    private val userIdProvider: UserIdProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query_key"
    }

    /** Dynamic argument generated at app start and injected via Hilt. */
    val currentUserId: String = userIdProvider.getOrCreate()

    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateQuery(newQuery: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = newQuery
    }

    fun performSearch() {
        val query = searchQuery.value
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        _uiState.value = SearchUiState.Loading

        viewModelScope.launch {
            Log.d("SearchVM", "search start vm=${this@SearchViewModel.hashCode()} q=$query")
            try {
                when (val result = searchCharactersUseCase(query)) {
                    is Result.Success -> {
                        Log.d("SearchVM", "Success: ${result.data.size} items, cache=${result.isFromCache}")
                        _uiState.value = SearchUiState.Success(
                            characters = result.data,
                            isFromCache = result.isFromCache,
                            isMessageConsumed = false
                        )
                    }
                    is Result.Error -> {
                        Log.w("SearchVM", "Error: ${result.message}")
                        _uiState.value = SearchUiState.Error(result.message)
                    }
                }
            } catch (t: Throwable) {
                Log.e("SearchVM", "search threw", t)
                throw t
            }
        }
    }

    fun consumeMessage() {
        val currentState = _uiState.value
        if (currentState is SearchUiState.Success) {
            _uiState.value = currentState.copy(isMessageConsumed = true)
        }
    }
}
