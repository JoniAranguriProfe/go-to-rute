package com.educacionit.gotorute.home.model.maps.api

import com.educacionit.gotorute.home.model.maps.PlaceSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchServiceAPI {
    @GET("/search.php")
    suspend fun getPlacesFromSearch(
        @Query("q") placeToSearch: String,
        @Query("format") format: String = "jsonv2"
    ): Response<List<PlaceSearchResponse>>

}