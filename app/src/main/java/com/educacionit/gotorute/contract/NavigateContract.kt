package com.educacionit.gotorute.contract

import android.content.Context
import com.educacionit.gotorute.home.model.maps.NavigateRepository
import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.model.maps.Point
import com.google.android.gms.maps.model.LatLng

interface NavigateContract {
    interface NavigateView<T : BaseContract.IBaseView> : FragmentBaseContract.IFragmentBaseView<T> {
        fun showSearchResults(placeResults: List<Place>)
        fun drawRoute(route: List<LatLng>)
    }

    interface IFragmentNavigatePresenter<T : FragmentBaseContract.IFragmentBaseView<*>> :
        FragmentBaseContract.IFragmentBasePresenter<T> {
        fun performPlacesSearch(placeToSearch: String)
        fun getRouteToPlace(destinationPlace: Place)
        suspend fun getCurrentPointPosition(): Point?
        fun startListeningLocation()
        fun stopListeningLocation()
        fun startCheckingDistanceToRoute()
        fun stopCheckingDistanceToRoute()
    }

    interface NavigateModel {
       suspend fun getPlacesFromSearch(placeToSearch: String): List<Place>
        suspend fun getRouteToPlace(startPlace: Place, destinationPlace: Place): List<Point>
        fun getCurrentPointPosition(): Point?
        fun initFusedLocationProviderClient(context: Context)
        fun stopListeningLocation()
        fun startListeningLocation(locationListener: NavigateRepository.OnNewLocationListener)
        fun startCheckingDistanceToRoute(context: Context)
        fun stopCheckingDistanceToRoute(context: Context)
        fun registerRouteNavigationAlarm(context: Context?)
        fun startCheckingBatteryStatus(context: Context?)
    }
}