package com.example.appcargamostest.application

interface OnMovieClickListener<T> {
    fun onMovieClick(movie: T, position: Int)
}