package com.example.demo.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListResponseDto<T>(
    val dates: Dates?,
    val page: Long?,
    val results: List<T>?,
    @SerialName("total_pages")
    val totalPages: Long?,
    @SerialName("total_results")
    val totalResults: Long?,
) {
    @Serializable
    data class Dates(
        val maximum: String?,
        val minimum: String?,
    )
}