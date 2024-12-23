package com.example.demo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.demo.data.local.dao.MovieDao
import com.example.demo.data.local.dao.RemoteKeyDao
import com.example.demo.data.local.entity.MovieEntity
import com.example.demo.data.local.entity.RemoteKeyEntity

@Database(
    version = 1,
    entities = [MovieEntity::class, RemoteKeyEntity::class],
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}