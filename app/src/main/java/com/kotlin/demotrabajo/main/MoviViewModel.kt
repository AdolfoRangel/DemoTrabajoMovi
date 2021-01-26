package com.kotlin.demotrabajo.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.demotrabajo.api.Movie
import com.kotlin.demotrabajo.api.service
import com.kotlin.demotrabajo.database.getDatabase
import com.kotlin.demotrabajo.main.MainActivity.Companion.applicationContext
import com.kotlin.demotrabajo.utils.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MoviViewModel(application: Application, val categoria: String, val internet: Boolean) :
    ViewModel() {
    private val TAG = MoviAdaptar::class.java.simpleName

    companion object {
        const val API_KEY = "0cc023827599a44a5198042881291290"
        const val LENGUAGE = "en-US"
    }

    private val database = getDatabase(application)
    private val repository = MainRepository(database)

    private val _status = MutableLiveData<ApiResponseStatus>()
    val status: LiveData<ApiResponseStatus>
        get() = _status

    private var _moviList = MutableLiveData<MutableList<Movie>>()
    val moviList: LiveData<MutableList<Movie>>
        get() = _moviList

    init {
        reloadMoviFromDatabase(categoria)
        Log.d(TAG, "init")

    }

    fun actualizarLista(categoria: String) {
        viewModelScope.launch {
            val util = Util()
            if (util.internet(applicationContext())) {
                reloadMovi(categoria)
            } else {
                reloadMoviFromDatabase(categoria)
                Log.d("MoviViewModel", "Sin internet")
            }
        }
    }

    private fun reloadMovi(categoria: String) {
        viewModelScope.launch {
            try {
                _status.value = ApiResponseStatus.LOADING
                _moviList.value = repository.fetchMovis(categoria, applicationContext())
                _status.value = ApiResponseStatus.DONE
            } catch (e: UnknownHostException) {
                Log.d(TAG, "No hay internet")
                _status.value = ApiResponseStatus.NOT_INTERNET_CONECCTION
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "No hay internet, tiempo excedido")
                _status.value = ApiResponseStatus.NOT_INTERNET_CONECCTION
            } catch (e: Error) {
                _status.value = ApiResponseStatus.ERROR
            }
        }
    }

    fun reloadMoviFromDatabase(categoria: String) {
        viewModelScope.launch {
            _moviList.value = repository.fetchMovieFromDataBase(categoria)
            if (_moviList.value!!.isEmpty()) {
                val util = Util()
                if (util.internet(applicationContext())) {
                    reloadMovi(categoria)
                } else {
                    Log.d("MoviViewModel", "Sin internet")
                }
            }
        }
    }


}