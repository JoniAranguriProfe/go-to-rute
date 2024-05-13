package com.educacionit.gotorute.contract

import com.educacionit.gotorute.congrats_route.model.ResultDBOperation
import com.educacionit.gotorute.home.model.maps.Place

interface CongratsContract {
    interface CongratsView : BaseContract.IBaseView {
        fun notifyFavoriteSaved()
    }

    interface ICongratsPresenter<T : BaseContract.IBaseView> : BaseContract.IBasePresenter<T> {
        fun saveFavoriteRoute(startPlace: Place?, destinationPlace: Place?)
        fun getCurrentDateFormatted(): String
    }

    interface CongratsModel {
        suspend fun saveFavoriteRoute(
            startPlace: Place,
            destinationPlace: Place,
            date: String
        ): ResultDBOperation
    }

}