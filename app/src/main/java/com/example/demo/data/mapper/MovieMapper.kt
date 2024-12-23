package com.example.demo.data.mapper

import com.example.demo.data.local.entity.MovieEntity
import com.example.demo.data.remote.dto.MovieDto
import com.example.demo.domain.model.Movie
import com.example.demo.orZero

fun MovieDto.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id.orZero(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
    )
}

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id.orZero(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
    )
}