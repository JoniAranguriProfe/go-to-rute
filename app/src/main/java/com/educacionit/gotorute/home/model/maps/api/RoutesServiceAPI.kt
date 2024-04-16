package com.educacionit.gotorute.home.model.maps.api

import com.educacionit.gotorute.home.model.maps.RouteSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RoutesServiceAPI {
    @GET(ROUTE_PATH)
    suspend fun getRouteToPlace(
        @Query(ORIGIN_QUERY) origin: String,
        @Query(DESTINATION_QUERY) destination: String,
        @Header(HEADER_API_KEY) apiKey: String = ROUTES_API_KEY,
        @Header(HEADER_HOST) apiHost: String = ROUTES_HOST
    ): Response<RouteSearchResponse>

}

const val ROUTE_PATH = "/FindDrivingPath"
const val ORIGIN_QUERY = "origin"
const val DESTINATION_QUERY = "destination"
const val HEADER_API_KEY = "X-RapidAPI-Key"
const val HEADER_HOST = "X-RapidAPI-Host"

const val ROUTES_API_KEY = "99fdb5f4e1msh76a5bdfb41eaf0ap16502fjsnfda5b4917134"
const val ROUTES_HOST = "trueway-directions2.p.rapidapi.com"
