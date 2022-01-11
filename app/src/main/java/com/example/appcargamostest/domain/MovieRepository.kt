package com.example.appcargamostest.domain

import com.example.appcargamostest.data.model.MovieList

interface MovieRepository {

    suspend fun getPopularMovies(): MovieList
    suspend fun getPopularSeries(): MovieList

}