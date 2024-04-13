package com.educacionit.gotorute.contract

import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.model.maps.Point
import com.google.android.gms.maps.model.LatLng

interface NavigateContract {
    interface NavigateView<T : BaseContract.IBaseView> : FragmentBaseContract.IFragmentBaseView<T> {
        fun showSearchResults(placeResults: List<Place>)
        fun drawRoute(route: List<LatLng>)
    }

    interface INavigatePresenter<T : FragmentBaseContract.IFragmentBaseView<*>> :
        FragmentBaseContract.IBasePresenter<T> {
        fun performPlacesSearch(placeToSearch: String)
        fun getRouteToPlace(destinationPlace: Place)
        suspend fun getCurrentPointPosition(): Point
    }

    interface NavigateModel {
        fun getPlacesFromSearch(placeToSearch: String): List<Place>
        fun getRouteToPlace(startPlace: Place, destinationPlace: Place): List<Point>
        fun getCurrentPointPosition(): Point
    }
}