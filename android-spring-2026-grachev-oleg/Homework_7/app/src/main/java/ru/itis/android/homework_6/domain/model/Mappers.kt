package ru.itis.android.homework_6.domain.model

import ru.itis.android.homework_6.data.model.CharacterDto
import ru.itis.android.homework_6.data.model.EpisodeDto
import ru.itis.android.homework_6.data.model.LocationDto

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        imageUrl = image,
        episodeUrls = episode
    )
}

fun LocationDto.toDomain(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension,
        residentUrls = residents
    )
}

fun EpisodeDto.toDomain(): Episode {
    return Episode(
        id = id,
        name = name,
        airDate = airDate,
        episodeCode = episodeCode,
        characterUrls = characters
    )
}