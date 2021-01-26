package com.kotlin.demotrabajo.main

import android.R
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.kotlin.demotrabajo.api.Movie
import com.kotlin.demotrabajo.api.service
import com.kotlin.demotrabajo.database.MoviDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainRepository (private val database:MoviDataBase) {
    private val TAG = MoviAdaptar::class.java.simpleName


    suspend fun fetchMovis(categoria: String,context: Context) : MutableList<Movie>{
        return withContext(Dispatchers.IO){
            val moviListString = service.getMovies(
                categoria,
                MoviViewModel.API_KEY,
                MoviViewModel.LENGUAGE,1)

            Log.d(TAG,"entro")
            Log.d(TAG,moviListString)

            val lsitaDemo = parseEqResult(moviListString, categoria,context)

            database.movie.insertAll(lsitaDemo)
            fetchMovieFromDataBase(categoria)
        }
    }

    suspend fun fetchMovieFromDataBase(categoria: String) : MutableList<Movie>{
        return  withContext(Dispatchers.IO){
            database.movie.getPeliculasCategoria(categoria)
        }
    }



    private fun parseEqResult(eqListString: String, categoria:String, context: Context): MutableList<Movie> {
        val eqJsonObject = JSONObject(eqListString)
        var eqList = mutableListOf<Movie>()

        if(eqListString.contains("results") ) {
            val featuresJsonArray = eqJsonObject.getJSONArray("results")



            for (i in 0 until featuresJsonArray.length()) {
                val feautesJSONObject = featuresJsonArray[i] as JSONObject
                val id = feautesJSONObject.getString("id")
                val name = feautesJSONObject.getString("original_title")
                val img = feautesJSONObject.getString("poster_path")
                val descripcion = feautesJSONObject.getString("overview")
                val adultos = feautesJSONObject.getString("adult")
                val resumen = feautesJSONObject.getString("overview")
                val fecha_estreno = feautesJSONObject.getString("release_date")

                val bitmap = getBitmapFromURL(img,context)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val byteArray = stream.toByteArray()

                val earthquake = Movie(
                        id,
                        name,
                        img,
                        descripcion,
                        adultos,
                        categoria,
                        byteArray,
                        resumen,
                        fecha_estreno
                    )

                eqList.add(earthquake)
            }
        }else {
            val id = eqJsonObject.getString("id")
            val name = eqJsonObject.getString("original_title")
            val img = eqJsonObject.getString("poster_path")
            val descripcion = eqJsonObject.getString("overview")
            val adultos = eqJsonObject.getString("adult")
            val resumen = eqJsonObject.getString("overview")
            val fecha_estreno = eqJsonObject.getString("release_date")

            val bitmap = getBitmapFromURL(img,context)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            val byteArray = stream.toByteArray()

            val earthquake = Movie(
                id,
                name,
                img,
                descripcion,
                adultos,
                categoria,
                byteArray,
                resumen,
                fecha_estreno
            )
            eqList.add(earthquake)
        }
        return eqList
    }

    fun getBitmapFromURL(src: String, context: Context): Bitmap {
        try {
            val url = URL("https://www.themoviedb.org/t/p/w220_and_h330_face"+src)
            val connection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            Log.d(TAG,"Imagen descargada")
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_menu_camera);
        }

    }



}