package com.educacionit.gotorute.home.presenter.maps

import com.educacionit.gotorute.R
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.NavigateRepository
import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.model.maps.Point
import com.educacionit.gotorute.utils.MapsManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow

class NavigatePresenter(private val navigateModel: NavigateContract.NavigateModel) :
    NavigateContract.IFragmentNavigatePresenter<NavigateContract.NavigateView<BaseContract.IBaseView>> {
    private lateinit var navigateView: NavigateContract.NavigateView<BaseContract.IBaseView>
    private var navigationState: NavigationState = NotNavigating
    private var locationListener: NavigateRepository.OnNewLocationListener? = null

    override fun attachView(view: NavigateContract.NavigateView<BaseContract.IBaseView>) {
        this.navigateView = view
        navigateView.getParentView()?.getViewContext()?.let { safeContext ->
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
        stopCurrentNavigation()
        CoroutineScope(Dispatchers.IO).launch {
            val currentPosition = getCurrentPointPosition()
            currentPosition?.let {
                val startPlace = Place("MyPosition", currentPosition)
                val route = navigateModel.getRouteToPlace(startPlace, destinationPlace).map {
                    LatLng(it.latitude, it.longitude)
                }
                withContext(Dispatchers.Main) {
                    navigationState = NavigationStarted(route = route)
                    startNavigation()
                    navigateView.drawRoute(route)
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    navigateView.getParentView()?.showErrorMessage(
                        navigateView.getParentView()?.getViewContext()?.resources?.getString(
                            R.string.cannot_get_current_location
                        ) ?: ""
                    )
                }
                navigationState = NotNavigating
            }
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
        locationListener = object : NavigateRepository.OnNewLocationListener {
            override fun currentLocationUpdate(point: Point) {
                when (navigationState) {
                    NotNavigating -> {
                        // Do nothing
                    }

                    is NavigationStarted -> {
                        updateDrawnRoute(
                            point,
                            (navigationState as? NavigationStarted)?.route
                        )
                    }
                }
            }

        }
        locationListener?.let { navigateModel.startListeningLocation(it) }
    }

    private fun updateDrawnRoute(point: Point, route: List<LatLng>?) {
        CoroutineScope(Dispatchers.Default).launch {
            val finalRoute: List<LatLng> = route?.let { getUpdatedRoute(point, it) } ?: emptyList()
            withContext(Dispatchers.Main) {
                navigateView.drawRoute(finalRoute)
            }
        }
    }

    private fun getUpdatedRoute(userLocation: Point, route: List<LatLng>): List<LatLng> {
        val nearestPointIndex = MapsManager.findNearestPointIndex(userLocation, route)

        val updatedRoute = if (nearestPointIndex >= 0 && nearestPointIndex < route.size) {
            route.subList(nearestPointIndex, route.size)
        } else {
            route
        }
        return updatedRoute
    }


    override fun stopListeningLocation() {
        navigateModel.stopListeningLocation()
    }

    override fun startNavigation() {
        navigateView.getParentView()?.getViewContext()?.let { safeContext ->
            navigateModel.startCheckingDistanceToRoute(safeContext)
            navigateModel.registerRouteNavigationAlarm(safeContext)
            navigateModel.startCheckingBatteryStatus(safeContext)
        }
    }

    override fun stopCurrentNavigation() {
        if(navigationState is NavigationStarted){
            navigateView.getParentView()?.getViewContext()?.let { safeContext ->
                navigateModel.stopCheckingDistanceToRoute(safeContext)
                navigateModel.removeNavigationAlarm(safeContext)
                navigateModel.stopCheckingBatteryStatus(safeContext)
            }
        }
    }

    companion object {
        const val CURRENT_POINT_MAX_RETRIES = 4
    }
}