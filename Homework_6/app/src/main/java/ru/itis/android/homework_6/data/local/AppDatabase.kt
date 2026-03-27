package ru.itis.android.homework_6.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.itis.android.homework_6.data.local.dao.CharacterDao
import ru.itis.android.homework_6.data.local.entity.CharacterEntity
import ru.itis.android.homework_6.data.local.entity.SearchQueryEntity

@Database(
    entities = [CharacterEntity::class, SearchQueryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun characterDao(): CharacterDao
}
