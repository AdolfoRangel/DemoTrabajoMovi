package com.kotlin.demotrabajo.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.demotrabajo.DetailMovie
import com.kotlin.demotrabajo.R
import com.kotlin.demotrabajo.api.Movie
import com.kotlin.demotrabajo.databinding.ActivityMainBinding
import com.kotlin.demotrabajo.utils.Util

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private lateinit var viewModel: MoviViewModel

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    val BASE_URL = "https://api.themoviedb.org";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val context: Context = MainActivity.applicationContext()


        binding.moviRecycler.layoutManager = LinearLayoutManager(this)

        val adpater = MoviAdaptar()
        binding.moviRecycler.adapter = adpater

        val util = Util()
        val internet = util.internet(this)

        viewModel = ViewModelProvider(this, MoviViewModelFactory(application,"upcoming",internet)).get(MoviViewModel::class.java)

        viewModel.moviList.observe(this, Observer {
            moviList ->
            adpater.submitList(moviList)

            handleEmptuView(moviList,binding)
        })

        viewModel.status.observe(this, Observer {
                apiResponseStatus ->
            if(apiResponseStatus == ApiResponseStatus.LOADING){
                binding.loagindProgressbar.visibility = View.VISIBLE
            }else if(apiResponseStatus == ApiResponseStatus.DONE){
                binding.loagindProgressbar.visibility = View.GONE
            }else if(apiResponseStatus == ApiResponseStatus.ERROR){
                binding.loagindProgressbar.visibility = View.GONE
            }
        })

        adpater.onItemClickListener={
            movi->
            Toast.makeText(this,"Alguna pelicula tocaste", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, DetailMovie::class.java)
            intent.putExtra(DetailMovie.DETALLE,movi)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == R.id.latest){
            viewModel.actualizarLista("latest")
        }else if(itemId == R.id.popular) {
            viewModel.actualizarLista("popular")
        }else if(itemId == R.id.upcoming){
            viewModel.actualizarLista("upcoming")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleEmptuView(
        moviList: MutableList<Movie>,
        binding: ActivityMainBinding
    ) {
        if (moviList.isEmpty()) {
            Log.d(TAG,"Pado aqui cuando esta vacio")
            binding.moviEmptyView.visibility = View.VISIBLE
        } else {
            Log.d(TAG,"Pado aqui")
            binding.moviEmptyView.visibility = View.GONE
        }
    }

    init {
        instance = this
    }


}