package com.educacionit.gotorute.congrats_route.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute
import com.educacionit.gotorute.congrats_route.model.db.entities.TABLE_FAVORITE

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM $TABLE_FAVORITE")
    fun getFavorites(): List<FavoriteRoute>

    @Insert
    fun addFavorite(favorite: FavoriteRoute)
}