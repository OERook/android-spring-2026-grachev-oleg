package ru.itis.android.homework_6.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.usecase.SearchCharacterUseCase

class SearchViewModel(
    private val searchCharactersUseCase: SearchCharacterUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query_key"
    }

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
            when (val result = searchCharactersUseCase(query)) {
                is Result.Success -> {
                    _uiState.value = SearchUiState.Success(
                        characters = result.data,
                        isFromCache = result.isFromCache,
                        isMessageConsumed = false
                    )
                }
                is Result.Error -> {
                    _uiState.value = SearchUiState.Error(result.message)
                }
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