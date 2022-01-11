package com.example.appcargamostest.domain

import com.example.appcargamostest.core.InternetCheck
import com.example.appcargamostest.data.local.LocalMovieDataSource
import com.example.appcargamostest.data.model.isNull
import com.example.appcargamostest.data.model.MovieList
import com.example.appcargamostest.data.model.toMovieEntity
import com.example.appcargamostest.data.remote.RemoteMovieDataSource

class MovieRepositoryImpl(
    private val dataSourceRemote: RemoteMovieDataSource,
    private val dataSourceLocal: LocalMovieDataSource
    ) : MovieRepository {

    override suspend fun getPopularMovies(): MovieList {
        return if (InternetCheck.isNetworkAvailable()) {
            dataSourceRemote.getPopularMovies().results.forEach { movie ->
                if (!movie.isNull()) dataSourceLocal.saveMovie(movie.toMovieEntity("movie_popular"))
            }
            dataSourceLocal.getPopularMovies()
        } else {
            dataSourceLocal.getPopularMovies()
        }
    }

    override suspend fun getPopularSeries(): MovieList {
        return if (InternetCheck.isNetworkAvailable()) {
        dataSourceRemote.getPopularSeries().results.forEach { movie ->
            if (!movie.isNull()) dataSourceLocal.saveMovie(movie.toMovieEntity("upcoming"))
        }
            dataSourceLocal.getPopularSeries()
        } else {
            dataSourceLocal.getPopularSeries()
        }
    }

}