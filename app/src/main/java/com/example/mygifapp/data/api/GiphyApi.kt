package com.example.mygifapp.data.api

import com.example.mygifapp.data.model.GiphyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {

    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): GiphyResponse
}