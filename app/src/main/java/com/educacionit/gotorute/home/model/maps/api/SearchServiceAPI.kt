package com.educacionit.gotorute.home.model.maps.api

import com.educacionit.gotorute.home.model.maps.PlaceSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchServiceAPI {
    @GET(SEARCH_PATH)
    suspend fun getPlacesFromSearch(
        @Query(PLACE_QUERY) placeToSearch: String,
        @Query(FORMAT_QUERY) format: String = DEFAULT_FORMAT
    ): Response<List<PlaceSearchResponse>>

}

const val SEARCH_PATH = "/search.php"
const val PLACE_QUERY = "q"
const val FORMAT_QUERY = "format"
const val DEFAULT_FORMAT = "jsonv2"
