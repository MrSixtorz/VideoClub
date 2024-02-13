package com.example.videoclub

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicioRetrofit {
    //corrutina
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun listPopularMovies(
        @Query("api_key") apiKey : String,
        @Query("region") region: String
    ) : DiscoverMovies
}

object RetrofitServiceFactory{
    fun makeRetrofitService():ServicioRetrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ServicioRetrofit::class.java)

    }
}