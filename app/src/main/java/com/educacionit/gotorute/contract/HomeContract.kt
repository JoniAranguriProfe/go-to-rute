package com.educacionit.gotorute.contract

import com.educacionit.gotorute.home.model.maps.Place

interface HomeContract {
    interface HomeView : BaseContract.IBaseView {
        fun openMapsScreenWithDestination(destinationPlace: Place)
    }

}