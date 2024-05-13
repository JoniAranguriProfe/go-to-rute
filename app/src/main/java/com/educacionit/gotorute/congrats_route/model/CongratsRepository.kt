package com.educacionit.gotorute.congrats_route.model

import android.content.Context
import androidx.room.Room
import com.educacionit.gotorute.congrats_route.model.db.DB_NAME
import com.educacionit.gotorute.congrats_route.model.db.FavoritesDao
import com.educacionit.gotorute.congrats_route.model.db.FavoritesDatabase
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute
import com.educacionit.gotorute.contract.CongratsContract
import com.educacionit.gotorute.home.model.maps.Place
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CongratsRepository(context: Context) : CongratsContract.CongratsModel {
    override suspend fun saveFavoriteRoute(startPlace: Place, destinationPlace: Place, date: String) : ResultDBOperation{
        return addFavorite(
            FavoriteRoute(
                startPlace = startPlace,
                destinationPlace = destinationPlace,
                date = date
            )
        )
    }

    override suspend fun deleteFavoriteRoute(startPlace: Place, destinationPlace: Place) : ResultDBOperation{
        val favoriteRouteToDelete = FavoriteRoute(
            startPlace = startPlace,
            destinationPlace = destinationPlace,
        )
        return deleteFavorite(favoriteRouteToDelete)
    }

    private lateinit var favoritesDao: FavoritesDao

    init {
        try {
            val db = Room.databaseBuilder(context, FavoritesDatabase::class.java, DB_NAME).build()
            favoritesDao = db.favoritesDao()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getFavorites(): List<FavoriteRoute> = favoritesDao.getFavorites()

    suspend fun addFavorite(favorite: FavoriteRoute): ResultDBOperation = try {
        favoritesDao.addFavorite(favorite)
        ResultOk
    } catch (e: Exception) {
        e.printStackTrace()
        ResultError
    }

    suspend fun deleteFavorite(favorite: FavoriteRoute): ResultDBOperation = try {
        favoritesDao.deleteFavorite(favorite)
        ResultOk
    } catch (e: Exception) {
        e.printStackTrace()
        ResultError
    }

}

sealed class ResultDBOperation
data object ResultOk : ResultDBOperation()
data object ResultError : ResultDBOperation()