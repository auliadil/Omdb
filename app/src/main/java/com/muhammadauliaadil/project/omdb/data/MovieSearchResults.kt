package com.muhammadauliaadil.project.omdb.data

import com.google.gson.annotations.SerializedName

data class SearchResults constructor(

    @SerializedName("Response")
    var response: String? = null,

    @SerializedName("totalResults")
    var totalResults: String? = null,

    @SerializedName("Search")
    var search: List<SearchItem>? = null
)

data class SearchItem constructor(

    @SerializedName("Type")
    var type: String? = null,

    @SerializedName("Year")
    var year: String? = null,

    @SerializedName("imdbID")
    var imdbID: String? = null,

    @SerializedName("Poster")
    var poster: String? = null,

    @SerializedName("Title")
    var title: String? = null

)