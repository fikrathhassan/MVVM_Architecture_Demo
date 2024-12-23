package com.example.demo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "movie"
)
data class MovieEntity(
    @PrimaryKey val id: Long,
    val title: String?,
    val releaseDate: String?
)
