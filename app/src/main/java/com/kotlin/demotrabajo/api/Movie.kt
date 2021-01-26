package com.kotlin.demotrabajo.api

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.ByteArrayOutputStream

@Parcelize
@Entity(tableName = "peliculas")
data class Movie (
    @PrimaryKey val id: String,
    val name:String,
    val img:String,
    val descripcion:String,
    val adultos: String,
    val categoria: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,
    val resumen: String,
    val fecha_lanzamiento: String
):Parcelable