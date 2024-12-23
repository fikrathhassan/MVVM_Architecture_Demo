package com.example.demo.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.demo.data.api.ApiInterface
import com.example.demo.data.local.database.MovieDatabase
import com.example.demo.data.local.entity.MovieEntity
import com.example.demo.data.local.entity.RemoteKeyEntity
import com.example.demo.data.mapper.toMovieEntity
import com.example.demo.orZero
import com.skydoves.sandwich.suspendOnSuccess
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class MovieMediator(
    private val apiService: ApiInterface,
    private val movieDatabase: MovieDatabase
): RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }
        try {
            var isEndOfList = true
            apiService.getTopRated(page).suspendOnSuccess {
                isEndOfList = data.results?.isEmpty() ?: true
                movieDatabase.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        movieDatabase.remoteKeyDao().clearRemoteKeyList()
                        movieDatabase.movieDao().clearMovieList()
                    }

                    val prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page.minus(1)
                    val nextKey = if (isEndOfList) null else page.plus(1)
                    val keys = data.results?.map {
                        RemoteKeyEntity(
                            id = it.id.orZero(),
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    } ?: emptyList()
                    movieDatabase.remoteKeyDao().insertRemoteKeyList(keys)

                    val movies = data.results?.map {
                        it.toMovieEntity()
                    } ?: emptyList()
                    movieDatabase.movieDao().insertMovies(movies)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie ->
                movieDatabase.remoteKeyDao().getRemoteKeyById(movie.id)
            }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movie ->
                movieDatabase.remoteKeyDao().getRemoteKeyById(movie.id)
            }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                movieDatabase.remoteKeyDao().getRemoteKeyById(movieId)
            }
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, MovieEntity>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getClosestRemoteKey(state)
                remoteKey?.nextKey?.minus(1) ?: TMDB_STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKey.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                // end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    companion object {
        const val TMDB_STARTING_PAGE_INDEX = 1
    }
}