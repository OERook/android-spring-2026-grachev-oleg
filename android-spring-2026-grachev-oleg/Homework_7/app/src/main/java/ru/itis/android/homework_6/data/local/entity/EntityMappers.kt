package ru.itis.android.homework_6.data.local.entity

import ru.itis.android.homework_6.domain.model.Character


fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodeUrls = episodeUrls
    )
}

fun Character.toEntity(searchQuery: String): CharacterEntity {
    return CharacterEntity(
        id = id,
        query = searchQuery,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = originName,
        locationName = locationName,
        imageUrl = imageUrl,
        episodeUrls = episodeUrls
    )
}