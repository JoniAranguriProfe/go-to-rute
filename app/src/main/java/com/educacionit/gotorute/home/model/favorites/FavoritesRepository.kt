package com.educacionit.gotorute.home.model.favorites

import android.content.Context
import androidx.room.Room
import com.educacionit.gotorute.congrats_route.model.db.DB_NAME
import com.educacionit.gotorute.congrats_route.model.db.FavoritesDao
import com.educacionit.gotorute.congrats_route.model.db.FavoritesDatabase
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute
import com.educacionit.gotorute.contract.FavoritesContract

class FavoritesRepository(context: Context) : FavoritesContract.FavoritesModel {
    private lateinit var favoritesDao: FavoritesDao

    init {
        try {
            val db = Room.databaseBuilder(context, FavoritesDatabase::class.java, DB_NAME).build()
            favoritesDao = db.favoritesDao()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getFavorites(): List<FavoriteRoute> = favoritesDao.getFavorites()

}