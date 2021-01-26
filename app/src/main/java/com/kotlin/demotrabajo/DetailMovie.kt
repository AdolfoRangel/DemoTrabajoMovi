package com.kotlin.demotrabajo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlin.demotrabajo.api.Movie
import com.kotlin.demotrabajo.databinding.ActivityDetailMovieBinding
import kotlinx.android.synthetic.main.activity_detail_movie.*

class DetailMovie : AppCompatActivity() {
    companion object{
        const val DETALLE = "detalle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras!!

        val movie :Movie = bundle.getParcelable<Movie>(DETALLE)!!

        binding.heroName.text = movie.name
        val bmp : Bitmap = BitmapFactory.decodeByteArray(movie.image, 0, movie.image.size)
        binding.imageView.setImageBitmap(bmp)
        binding.descripcion.text = movie.descripcion
        binding.fecha.text = movie.fecha_lanzamiento
    }
}