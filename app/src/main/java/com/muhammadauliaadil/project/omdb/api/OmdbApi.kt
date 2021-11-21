package com.muhammadauliaadil.project.omdb.api

import com.muhammadauliaadil.project.omdb.data.Movie
import com.muhammadauliaadil.project.omdb.data.SearchResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "9378869f"

interface OmdbApi {

    @GET("/")
    fun getSearchResultData(
        @Query("s") searchTitle: String,
        @Query("page") pageIndex: Int,
        @Query("apiKey") apiKey: String = API_KEY)

            : Call<SearchResults>

    @GET("?plot=full")
    fun getMovieDetailData(
        @Query("t") title: String,
        @Query("apiKey") apiKey: String = API_KEY)
            : Call<Movie>

}