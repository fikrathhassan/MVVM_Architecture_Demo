package com.example.demo.di

import com.example.demo.data.remote.MovieRemoteDataSourceImpl
import com.example.demo.domain.datasource.MovieDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindMovieDataSource(impl: MovieRemoteDataSourceImpl): MovieDataSource
}