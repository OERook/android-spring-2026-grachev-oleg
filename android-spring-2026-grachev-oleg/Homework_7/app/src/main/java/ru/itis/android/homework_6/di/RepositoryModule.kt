package ru.itis.android.homework_6.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.android.homework_6.data.repository.CharacterRepositoryImpl
import ru.itis.android.homework_6.domain.repository.CharacterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository
}
