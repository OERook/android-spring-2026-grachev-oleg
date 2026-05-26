package ru.itis.android.homework_6.presentation.search

import android.os.Message
import ru.itis.android.homework_6.domain.model.Character

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState

    data class Success(
        val characters: List<Character>,
        val isFromCache: Boolean,
        val isMessageConsumed: Boolean = false
    ) : SearchUiState
    data class Error(val message: String) : SearchUiState
}