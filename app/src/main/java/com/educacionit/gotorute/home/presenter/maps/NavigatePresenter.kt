package com.educacionit.gotorute.home.presenter.maps

import com.educacionit.gotorute.R
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.model.maps.Point
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow

class NavigatePresenter(private val navigateModel: NavigateContract.NavigateModel) :
    NavigateContract.IFragmentNavigatePresenter<NavigateContract.NavigateView<BaseContract.IBaseView>> {
    private lateinit var navigateView: NavigateContract.NavigateView<BaseContract.IBaseView>

    override fun attachView(view: NavigateContract.NavigateView<BaseContract.IBaseView>) {
        this.navigateView = view
        navigateView.getParentView()?.getViewContext()?.let {safeContext ->
            navigateModel.initFusedLocationProviderClient(safeContext)
            startListeningLocation()
        }
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
            val currentPosition = getCurrentPointPosition()
            currentPosition?.let {
                val startPlace = Place("MyPosition", currentPosition)
                val route = navigateModel.getRouteToPlace(startPlace, destinationPlace).map {
                    LatLng(it.latitude, it.longitude)
                }
                withContext(Dispatchers.Main) {
                    navigateView.drawRoute(route)
                }
            } ?: navigateView.getParentView()?.showErrorMessage(
                navigateView.getParentView()?.getViewContext()?.resources?.getString(
                    R.string.cannot_get_current_location
                ) ?: ""
            )
        }
    }

    override suspend fun getCurrentPointPosition(): Point? {
        var currentPosition: Point? = navigateModel.getCurrentPointPosition()
        var retryCount = 0

        while (currentPosition == null && retryCount < CURRENT_POINT_MAX_RETRIES) {
            // Exponential backoff delay
            val delayMillis = (2.toDouble().pow(retryCount.toDouble()) * 1000).toLong()
            delay(delayMillis)
            currentPosition = navigateModel.getCurrentPointPosition()
            retryCount++
        }

        return currentPosition
    }

    override fun startListeningLocation() {
        navigateModel.startListeningLocation()
    }

    override fun stopListeningLocation() {
        navigateModel.stopListeningLocation()
    }

    companion object {
        const val CURRENT_POINT_MAX_RETRIES = 4
    }
}