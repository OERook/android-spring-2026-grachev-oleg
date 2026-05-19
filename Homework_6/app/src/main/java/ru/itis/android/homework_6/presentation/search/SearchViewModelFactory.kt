package ru.itis.android.homework_6.presentation.search

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.itis.android.homework_6.MyApplication

val SearchViewModelFactory = viewModelFactory {
    initializer {

        val application = (this[APPLICATION_KEY] as MyApplication)
        val searchUseCase = application.container.searchCharacterUseCase

        val savedStateHandle = this.createSavedStateHandle()

        SearchViewModel(searchUseCase, savedStateHandle)
    }
}