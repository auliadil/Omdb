package com.muhammadauliaadil.project.omdb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadauliaadil.project.omdb.api.OmdbApi
import com.muhammadauliaadil.project.omdb.api.OmdbApiClient
import com.muhammadauliaadil.project.omdb.data.SearchItem
import com.muhammadauliaadil.project.omdb.data.SearchResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieSearchViewModel(private var currentPage: Int) : ViewModel() {

    private var omdbApi: OmdbApi = OmdbApiClient.getClient().create(OmdbApi::class.java)
    internal var searchResultsList: ArrayList<SearchItem> = ArrayList<SearchItem>()
    private var statusResponse: MutableLiveData<String>? = MutableLiveData("empty")

    fun getStatusResponse() = statusResponse

    fun getSearchResultMoviesData(query: String) {
        val call = omdbApi.getSearchResultData(query, currentPage)
        call.enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.isSuccessful) {
                    if (response.body()!!.response.equals("True")) {
                        searchResultsList.addAll(response.body()!!.search!!)
                        statusResponse?.value = "SUCCESS_FOUND"
                    } else {
                        statusResponse?.value = "SUCCESS_NOT_FOUND"
                    }
                } else {
                    statusResponse?.value = "SUCCESS_NOT_RESPONSE"
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                statusResponse?.value = "FAILURE"
            }
        })
    }

    fun loadNextPage(query: String) {
        val call = omdbApi.getSearchResultData(query, currentPage)
        call.enqueue(object : Callback<SearchResults> {
            override fun onResponse(call: Call<SearchResults>, response: Response<SearchResults>) {
                if (response.isSuccessful) {
                    if (response.body()!!.response.equals("True")) {
                        searchResultsList.addAll(response.body()!!.search!!)
                        statusResponse?.value = "SUCCESS_FOUND"
                    } else {
                        statusResponse?.value = "SUCCESS_NOT_FOUND"
                    }
                } else {
                    statusResponse?.value = "SUCCESS_NOT_RESPONSE"
                }
            }

            override fun onFailure(call: Call<SearchResults>, t: Throwable) {
                statusResponse?.value = "FAILURE"
            }
        })
    }
}