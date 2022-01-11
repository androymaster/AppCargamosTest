package com.example.appcargamostest.data.local

import androidx.lifecycle.LiveData
import com.example.appcargamostest.data.model.FavoriteMovie
import com.example.appcargamostest.data.model.MovieEntity
import com.example.appcargamostest.data.model.MovieList
import com.example.appcargamostest.data.model.toMovieList

class LocalMovieDataSource(private val movieDao: MovieDao) {

    suspend fun getPopularMovies(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "movie_popular" }.toMovieList()
    }

    suspend fun getPopularSeries(): MovieList {
        return movieDao.getAllMovies().filter { it.movie_type == "upcoming" }.toMovieList()
    }

    fun getFavoriteMovies(): LiveData<List<FavoriteMovie>> = movieDao.getAllFavoriteMovies()

    suspend fun saveMovie(movieEntity: MovieEntity) = movieDao.saveMovie(movieEntity)

    suspend fun saveFavoriteMovie(movie: FavoriteMovie) = movieDao.saveFavoriteMovie(movie)

    suspend fun deleteFavoriteMovie(movie: FavoriteMovie) = movieDao.deleteFavoriteMovie(movie)
}