package com.educacionit.gotorute.home.presenter.maps

import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.model.maps.Point
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NavigatePresenterFragment(private val navigateModel: NavigateContract.NavigateModel) :
    NavigateContract.IFragmentNavigatePresenter<NavigateContract.NavigateView<BaseContract.IBaseView>> {
    private lateinit var navigateView: NavigateContract.NavigateView<BaseContract.IBaseView>

    override fun attachView(view: NavigateContract.NavigateView<BaseContract.IBaseView>) {
        this.navigateView = view

    }

    override fun performPlacesSearch(placeToSearch: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val results = navigateModel.getPlacesFromSearch(placeToSearch)
            withContext(Dispatchers.Main) {
                navigateView.showSearchResults(results)
            }
        }
    }

    override fun getRouteToPlace(destinationPlace: Place) {
        CoroutineScope(Dispatchers.IO).launch {
            val startPlace = Place("MyPosition", getCurrentPointPosition())
            val route = navigateModel.getRouteToPlace(startPlace, destinationPlace).map {
                LatLng(it.latitude, it.longitude)
            }
            withContext(Dispatchers.Main) {
                navigateView.drawRoute(route)
            }
        }
    }

    override suspend fun getCurrentPointPosition(): Point {
        val job: Deferred<Point> = CoroutineScope(Dispatchers.IO).async {
            navigateModel.getCurrentPointPosition()
        }
        return job.await()
    }
}