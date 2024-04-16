package com.educacionit.gotorute.home.model.maps

import com.educacionit.gotorute.home.model.maps.api.SearchServiceAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {
    private const val SEARCH_SERVICE_BASE_URL = "https://nominatim.openstreetmap.org"

    val searchServiceAPI: SearchServiceAPI =
        getRetrofitInstance(SEARCH_SERVICE_BASE_URL).create(SearchServiceAPI::class.java)

    private fun getRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}