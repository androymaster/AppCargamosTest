package com.example.appcargamostest.data.remote

import com.example.appcargamostest.application.AppConstants
import com.example.appcargamostest.core.Resource
import com.example.appcargamostest.data.model.MovieList
import com.example.appcargamostest.domain.WebService

class RemoteMovieDataSource(private val webService: WebService) {

    suspend fun getPopularMovies(): MovieList {
        return webService.getPopularMovies(AppConstants.API_KEY)
    }

    suspend fun getPopularSeries(): MovieList {
        return webService.getPopularSeries(AppConstants.API_KEY)
    }
}