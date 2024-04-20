package com.educacionit.gotorute.home.model.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.educacionit.gotorute.contract.NavigateContract
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.lang.ref.WeakReference

class NavigateRepository : NavigateContract.NavigateModel {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var newLocationListener : WeakReference<OnNewLocationListener>
    var currentLocation: Point? = null

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
            mapRoute(rawRouteResponse.body()?.route?.geometry?.coordinates)
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