package com.educacionit.gotorute.home.model.maps

import com.google.gson.annotations.SerializedName

data class RouteSearchResponse(
    @SerializedName("route")
    val route: RouteRawResponse
)

data class RouteRawResponse(
    @SerializedName("geometry")
    val geometry: GeometryResponse
)

data class GeometryResponse(
    @SerializedName("coordinates")
    val coordinates: List<List<Double>>
)
