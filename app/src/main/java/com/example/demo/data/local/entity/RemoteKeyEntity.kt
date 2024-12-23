package com.example.demo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "remote_key"
)
data class RemoteKeyEntity(
    @PrimaryKey val id: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
