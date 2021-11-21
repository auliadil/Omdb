package com.muhammadauliaadil.project.omdb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadauliaadil.project.omdb.api.OmdbApi
import com.muhammadauliaadil.project.omdb.api.OmdbApiClient
import com.muhammadauliaadil.project.omdb.data.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {

    private var omdbApi: OmdbApi = OmdbApiClient.getClient().create(OmdbApi::class.java)
    var statusResponse: MutableLiveData<String>? = MutableLiveData("empty")
    var movie : Movie = Movie()

    fun getMovieData(query: String) {
        val call = omdbApi.getMovieDetailData(query, "69841868")
        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    movie.title= response.body()!!.title!!
                    movie.poster= response.body()!!.poster!!
                    movie.year= response.body()!!.year!!
                    movie.runtime = response.body()!!.director
                    movie.imdbrating = response.body()!!.imdbrating
                    movie.director = response.body()!!.director
                    movie.writer = response.body()!!.writer
                    movie.genre = response.body()!!.genre
                    movie.plot = response.body()!!.plot
                    statusResponse?.value = "SUCCESS_FOUND"
                }
                else {
                    statusResponse?.value = "SUCCESS_NOT_RESPONSE"
                }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable) {
                statusResponse?.value = "FAILURE"
            }
        })
    }

}