package com.example.demo.ui.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.demo.domain.datasource.MovieDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    movieDataSource: MovieDataSource
): ViewModel() {

    val movieList = movieDataSource.getMovies().cachedIn(viewModelScope)

}