package com.kotlin.demotrabajo.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET("/3/movie/{category}")
    suspend fun getMovies(
            @Path("category") category: String?,
            @Query("api_key") apiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int
    ): String


}

    private var retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    var service : ApiInterface = retrofit.create<ApiInterface>(
        ApiInterface::class.java)
