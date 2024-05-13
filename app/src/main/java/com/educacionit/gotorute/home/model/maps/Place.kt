package com.educacionit.gotorute.home.model.maps

import java.io.Serializable

data class Place(
    var displayName: String,
    val point: Point
) : Serializable {
    fun getFormattedName() = displayName.split(",").take(3).joinToString(",")
}
