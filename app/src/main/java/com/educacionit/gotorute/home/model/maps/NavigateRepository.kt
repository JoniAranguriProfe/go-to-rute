package com.educacionit.gotorute.home.model.maps

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.services.RouteCheckService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.lang.ref.WeakReference

class NavigateRepository : NavigateContract.NavigateModel {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var newLocationListener: WeakReference<OnNewLocationListener>
    private var routeCheckServiceConnection: ServiceConnection? = null
    private var isServiceBound = false
    private var routeCheckService: RouteCheckService ?= null
    var currentLocation: Point? = null
    var currentRoute: List<Point>? = null

    override suspend fun getPlacesFromSearch(placeToSearch: String): List<Place> {
        val response = ApiServiceProvider.searchServiceAPI.getPlacesFromSearch(placeToSearch)
        return response.body()?.let { mapPlaces(it) } ?: emptyList()
    }

    private fun mapPlaces(apiPlaces: List<PlaceSearchResponse>): List<Place> {
        return apiPlaces.map {
            Place(
                displayName = it.displayName,
                point = Point(it.latitude.toDouble(), it.longitude.toDouble())
            )
        }
    }

    override suspend fun getRouteToPlace(startPlace: Place, destinationPlace: Place): List<Point> {
        val rawRouteResponse = ApiServiceProvider.routesServiceAPI.getRouteToPlace(
            getFormattedPoints(startPlace),
            getFormattedPoints(destinationPlace)
        )
        return if (rawRouteResponse.isSuccessful) {
            val safeRoute = mapRoute(rawRouteResponse.body()?.route?.geometry?.coordinates)
            currentRoute = safeRoute
            safeRoute
        } else {
            emptyList()
        }
    }

    private fun getFormattedPoints(place: Place): String =
        "${place.point.latitude},${place.point.longitude}"

    private fun mapRoute(coordinates: List<List<Double>>?): List<Point> =
        coordinates?.map { Point(it.first(), it.last()) } ?: emptyList()

    override fun getCurrentPointPosition(): Point? = currentLocation

    override fun initFusedLocationProviderClient(context: Context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun startCheckingDistanceToRoute(context: Context) {
        if(isServiceBound){
            return
        }
        setupRouteServiceConnection()
        val intent = Intent(context, RouteCheckService::class.java)
        routeCheckServiceConnection?.let {
            context.bindService(intent, it, BIND_AUTO_CREATE)
        }
    }

    override fun stopCheckingDistanceToRoute(context: Context) {
        routeCheckServiceConnection?.let {
            context.unbindService(it)
        }
        isServiceBound = false
    }

    @SuppressLint("MissingPermission")
    override fun startListeningLocation(locationListener: OnNewLocationListener) {
        this.newLocationListener = WeakReference(locationListener)
        setupLocationCallback()
        val locationRequest = LocationRequest.Builder(LOCATION_UPDATES_INTERVAL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(LOCATION_FASTEST_UPDATES_INTERVAL)
            .build()
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                currentLocation =
                    locationResult.lastLocation?.let { Point(it.latitude, it.longitude) }
                currentLocation?.let {
                    newLocationListener.get()?.currentLocationUpdate(it)
                }
            }
        }
    }

    private fun setupRouteServiceConnection() {
        routeCheckServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as RouteCheckService.RouteCheckBinder
                routeCheckService = binder.getService()
                routeCheckService?.setLocationProvider(object : RouteCheckService.CurrentLocationStatusProvider{
                    override fun getCurrentLocation(): Point? {
                       return currentLocation
                    }

                    override fun getCurrentRoute(): List<Point>? {
                       return currentRoute
                    }

                }
                )
                isServiceBound = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isServiceBound = false
            }

        }
    }

    override fun stopListeningLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    interface OnNewLocationListener {
        fun currentLocationUpdate(point: Point)
    }

    companion object {
        const val LOCATION_UPDATES_INTERVAL = 1000L
        const val LOCATION_FASTEST_UPDATES_INTERVAL = 800L
    }
}