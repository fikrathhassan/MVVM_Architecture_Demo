package com.example.demo.data.api

import com.example.demo.data.remote.dto.MovieListResponseDto
import com.example.demo.data.remote.dto.MovieDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("page") page: Int
    ) : ApiResponse<MovieListResponseDto<MovieDto>>

}