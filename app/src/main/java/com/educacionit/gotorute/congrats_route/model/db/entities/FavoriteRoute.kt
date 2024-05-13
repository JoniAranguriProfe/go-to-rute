package com.educacionit.gotorute.congrats_route.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.educacionit.gotorute.home.model.maps.Place

@Entity(tableName = TABLE_FAVORITE)

data class FavoriteRoute(
    @ColumnInfo(name = COLUMN_START_PLACE)
    val startPlace: Place,
    @ColumnInfo(name = COLUMN_DESTINATION_PLACE)
    val destinationPlace: Place,
    @ColumnInfo(name = COLUMN_DATE)
    val date: String
){
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    var id: String = getRouteId()
    private fun getRouteId() = "${startPlace.displayName}-${destinationPlace.displayName}"
}

const val TABLE_FAVORITE = "favorite"
const val COLUMN_ID = "id"
const val COLUMN_START_PLACE = "start_place"
const val COLUMN_DESTINATION_PLACE = "destination_place"
const val COLUMN_DATE = "date"
