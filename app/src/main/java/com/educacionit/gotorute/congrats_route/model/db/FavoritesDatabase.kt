package com.educacionit.gotorute.congrats_route.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.educacionit.gotorute.congrats_route.model.db.converters.PlaceConverter
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute

@Database(entities = [FavoriteRoute::class], version = DB_VERSION)
@TypeConverters(PlaceConverter::class)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}

const val DB_VERSION = 1
const val DB_NAME = "favorites"
