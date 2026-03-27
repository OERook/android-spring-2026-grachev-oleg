package ru.itis.android.homework_6.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.android.homework_6.MyApplication
import ru.itis.android.homework_6.domain.model.Character
import ru.itis.android.homework_6.domain.model.Result
import ru.itis.android.homework_6.domain.usecase.GetCharacterDetailsUseCase

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(val character: Character, val isFromCache: Boolean) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class DetailsViewModel(
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

val DetailsViewModelFactory = viewModelFactory {
    initializer {
        val application = (this[APPLICATION_KEY] as MyApplication)
        val useCase = application.container.getCharacterDetailsUseCase
        val savedStateHandle = this.createSavedStateHandle()
        DetailsViewModel(useCase, savedStateHandle)
    }
}