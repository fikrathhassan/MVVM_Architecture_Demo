package com.example.demo.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.demo.data.api.ApiInterface
import com.example.demo.data.mapper.toMovie
import com.example.demo.domain.model.Movie
import com.example.demo.AppConstants
import com.skydoves.sandwich.suspendOnSuccess
import okio.IOException
import retrofit2.HttpException

class MoviesPagingSource(
    private val apiInterface: ApiInterface
): PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageIndex = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val movieList = mutableListOf<Movie>()
            apiInterface.getTopRated(page = pageIndex).suspendOnSuccess {
                val list = data.results?.map {
                    it.toMovie()
                } ?: emptyList()

                movieList.clear()
                movieList.addAll(list)
            }
            LoadResult.Page(
                data = movieList,
                prevKey = if (pageIndex == TMDB_STARTING_PAGE_INDEX) null else pageIndex.minus(1),
                nextKey = if (movieList.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / AppConstants.LIST_PAGE_SIZE)
                }
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val TMDB_STARTING_PAGE_INDEX = 1
    }
}