package com.educacionit.gotorute.home.model.maps

import com.google.gson.annotations.SerializedName

data class PlaceSearchResponse(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("lat")
    val latitude: String,
    @SerializedName("lon")
    val longitude: String,
)
