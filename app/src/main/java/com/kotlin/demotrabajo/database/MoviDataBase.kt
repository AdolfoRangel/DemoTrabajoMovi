package com.kotlin.demotrabajo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.demotrabajo.api.Movie

@Database(entities = [Movie::class],version = 1, exportSchema = false)
abstract class MoviDataBase: RoomDatabase(){
    abstract val movie: MoviDao
}

private lateinit var INSTANCE : MoviDataBase

fun getDatabase(context: Context): MoviDataBase{
    synchronized(MoviDataBase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviDataBase::class.java,
                "peliculas_db"
            ).build()
        }
        return INSTANCE
    }
}