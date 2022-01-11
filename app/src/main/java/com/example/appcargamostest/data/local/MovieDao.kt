package com.example.appcargamostest.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appcargamostest.data.model.FavoriteMovie
import com.example.appcargamostest.data.model.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM FavoriteMovie")
    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavoriteMovie(movie: FavoriteMovie)

    @Delete
    suspend fun deleteFavoriteMovie(movie: FavoriteMovie)

}