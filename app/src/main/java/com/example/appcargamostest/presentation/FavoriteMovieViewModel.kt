package com.example.appcargamostest.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcargamostest.data.local.LocalMovieDataSource
import com.example.appcargamostest.data.model.FavoriteMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteMovieViewModel (private val repo: LocalMovieDataSource) : ViewModel() {
    val favoriteMovieList: LiveData<List<FavoriteMovie>>

    init {
        favoriteMovieList = repo.getFavoriteMovies()
    }

    fun saveFavoriteMovie(movie: FavoriteMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveFavoriteMovie(movie)
        }
    }

    fun deleteFavoriteMovie(movie: FavoriteMovie) {
        viewModelScope.launch {
            repo.deleteFavoriteMovie(movie)
        }
    }
}

class FavoriteMovieViewModelFactory(private val repo: LocalMovieDataSource) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LocalMovieDataSource::class.java).newInstance(repo)
    }
}