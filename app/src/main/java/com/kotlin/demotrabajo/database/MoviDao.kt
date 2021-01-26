package com.kotlin.demotrabajo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kotlin.demotrabajo.api.Movie

@Dao
interface MoviDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Movie>)

    @Query("SELECT * FROM peliculas WHERE categoria = :CATEGORIA")
    fun getPeliculasCategoria(CATEGORIA: String): MutableList<Movie>

    @Query("SELECT * FROM peliculas")
    fun getPeliculas(): MutableList<Movie>
}