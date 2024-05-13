package com.educacionit.gotorute.contract

import com.educacionit.gotorute.congrats_route.model.ResultDBOperation
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute
import com.educacionit.gotorute.home.model.maps.Place

interface FavoritesContract {
    interface FavoritesView<T : BaseContract.IBaseView> : FragmentBaseContract.IFragmentBaseView<T> {
        fun setFavoritesItems(favorites: List<FavoriteRoute>)
    }

    interface IFavoritesPresenter<T : FragmentBaseContract.IFragmentBaseView<*>> :
        FragmentBaseContract.IFragmentBasePresenter<T> {
        fun getFavoriteRoutes()
    }

    interface FavoritesModel {
        suspend fun getFavorites(): List<FavoriteRoute>
    }

}