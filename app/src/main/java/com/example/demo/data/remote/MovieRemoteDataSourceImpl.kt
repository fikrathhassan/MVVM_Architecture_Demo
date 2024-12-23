package com.example.demo.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.demo.AppConstants
import com.example.demo.data.api.ApiInterface
import com.example.demo.data.paging.MoviesPagingSource
import com.example.demo.domain.datasource.MovieDataSource
import com.example.demo.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiInterface
) : MovieDataSource {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = AppConstants.LIST_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviesPagingSource(apiService)
            },
        ).flow
    }

}