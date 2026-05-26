package ru.itis.android.homework_6.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.usecase.GetCharacterDetailsUseCase
import javax.inject.Inject

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(val character: Character, val isFromCache: Boolean) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

/**
 * The characterId is selected at runtime when the user taps a list row.
 * Hilt provides SavedStateHandle that holds the navigation argument — this is
 * the "dynamic argument passed between screens via DI" required by Homework_7.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle["characterId"])

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            when (val result = getCharacterDetailsUseCase(characterId)) {
                is Result.Success -> _uiState.value = DetailsUiState.Success(result.data, result.isFromCache)
                is Result.Error -> _uiState.value = DetailsUiState.Error(result.message)
            }
        }
    }
}
