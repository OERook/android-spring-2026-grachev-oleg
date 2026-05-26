package ru.itis.android.homework_6.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.android.homework_6.data.local.entity.SearchQueryEntity
import ru.itis.android.homework_6.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchQuery(query: SearchQueryEntity)

    @Query("SELECT * FROM search_queries WHERE `query` = :query")
    suspend fun getSearchQuery(query: String): SearchQueryEntity?

    @Query("SELECT * FROM characters WHERE `query` = :query")
    suspend fun getCharactersByQuery(query: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?
}
