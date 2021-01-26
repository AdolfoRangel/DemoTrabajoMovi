package com.kotlin.demotrabajo.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviViewModelFactory(private val application: Application, private val category: String, private val internet:Boolean) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return MoviViewModel(application,category,internet)as T
    }
}