package com.educacionit.gotorute.contract

import com.educacionit.gotorute.congrats_route.model.ResultDBOperation
import com.educacionit.gotorute.home.model.maps.Place

interface CongratsContract {
    interface CongratsView : BaseContract.IBaseView {
        fun notifyFavoriteAction(message: String)
    }

    interface ICongratsPresenter<T : BaseContract.IBaseView> : BaseContract.IBasePresenter<T> {
        fun saveFavoriteRoute(startPlace: Place?, destinationPlace: Place?, isAdd: Boolean)
        fun getCurrentDateFormatted(): String
    }

    interface CongratsModel {
        suspend fun saveFavoriteRoute(
            startPlace: Place,
            destinationPlace: Place,
            date: String
        ): ResultDBOperation

        suspend fun deleteFavoriteRoute(
            startPlace: Place,
            destinationPlace: Place
        ): ResultDBOperation
    }

}