package ru.itis.android.homework_6.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.android.homework_6.data.local.AppDatabase
import ru.itis.android.homework_6.data.local.dao.CharacterDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "rick_and_morty_db").build()

    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao = db.characterDao()
}
