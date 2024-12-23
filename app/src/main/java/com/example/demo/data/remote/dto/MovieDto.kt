package com.example.demo.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Long?,
    @SerialName("release_date")
    val releaseDate: String?,
    val title: String?,
)
