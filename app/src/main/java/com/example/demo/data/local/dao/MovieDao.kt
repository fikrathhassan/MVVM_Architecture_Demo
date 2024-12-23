package com.example.demo.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.demo.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Upsert
    suspend fun insertMovies(data: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getMovieList(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie")
    suspend fun clearMovieList()

}