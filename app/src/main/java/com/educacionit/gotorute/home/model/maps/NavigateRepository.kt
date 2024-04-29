package com.educacionit.gotorute.home.model.maps

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.os.SystemClock
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.receivers.BatteryLowReceiver
import com.educacionit.gotorute.home.model.maps.services.RouteCheckService
import com.educacionit.gotorute.home.view.HomeActivity
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
    private lateinit var newLocationListener: WeakReference<OnNewLocationListener>
    private var routeCheckServiceConnection: ServiceConnection? = null
    private var isServiceBound = false
    private var routeCheckService: RouteCheckService? = null
    var currentLocation: Point? = null
    var currentRoute: List<Point>? = null
    private var batteryLowReceiver: BatteryLowReceiver? = null

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
        if (isServiceBound) {
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

    override fun registerRouteNavigationAlarm(context: Context?) {
        context?.let { safeContext ->
            val alarmManager =
                safeContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(safeContext, HomeActivity::class.java)
            alarmIntent.action = ACTION_NAVIGATION_ALARM
            val pendingIntent = PendingIntent.getActivity(
                safeContext, 0, alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 10000,
                pendingIntent
            )
        }
    }

    override fun startCheckingBatteryStatus(context: Context?) {
        context?.let { safeContext ->
            batteryLowReceiver = BatteryLowReceiver()
            val batteryIntentFilter = IntentFilter(Intent.ACTION_BATTERY_LOW)
            safeContext.registerReceiver(batteryLowReceiver, batteryIntentFilter)
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
                routeCheckService?.setLocationProvider(object :
                    RouteCheckService.CurrentLocationStatusProvider {
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
        const val ACTION_NAVIGATION_ALARM = "com.educacionit.gotorute.ACTION_NAVIGATION_ALARM"
    }
}