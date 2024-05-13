package com.educacionit.gotorute.congrats_route.model.db.converters

import androidx.room.TypeConverter
import com.educacionit.gotorute.home.model.maps.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaceConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): Place {
        val type = object : TypeToken<Place>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(route: Place): String {
        return gson.toJson(route)
    }
}