package com.example.demo.domain.datasource

import androidx.paging.PagingData
import com.example.demo.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {

    fun getMovies(): Flow<PagingData<Movie>>

}