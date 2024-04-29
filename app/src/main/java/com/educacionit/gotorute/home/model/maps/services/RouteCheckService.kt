package com.educacionit.gotorute.home.model.maps.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.educacionit.gotorute.home.model.maps.Point
import com.educacionit.gotorute.utils.MapsManager
import com.educacionit.gotorute.utils.notifications.NotificationOutOfRoute
import com.educacionit.gotorute.utils.notifications.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RouteCheckService : Service() {

    private val routeCheckBinder = RouteCheckBinder()
    private var notificationManager: NotificationManager? = null
    private var isOutOfRoute = false
    private var routeCheckJob: Job? = null
    private val checkIntervalMs = 5000L
    private var locationProvider: CurrentLocationStatusProvider? = null
    private var notificationShown = false

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        startRouteCheck()
    }

    fun setLocationProvider(locationProvider: CurrentLocationStatusProvider) {
        this.locationProvider = locationProvider
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRouteCheck()
    }

    override fun onBind(intent: Intent?): IBinder {
        return routeCheckBinder
    }

    inner class RouteCheckBinder : Binder() {
        fun getService() = this@RouteCheckService
    }

    private fun startRouteCheck() {
        routeCheckJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                isOutOfRoute = checkIfOutOfRoute()
                if (isOutOfRoute && !notificationShown) {
                    showNotification()
                    stopSelf()
                }
                delay(checkIntervalMs)
            }
        }
    }

    private fun stopRouteCheck() {
        routeCheckJob?.cancel()
    }

    private fun checkIfOutOfRoute(): Boolean {
        return locationProvider?.let {
            val currentLocation = it.getCurrentLocation()
            val currentRoute = it.getCurrentRoute()
            return currentRoute?.let {
                currentLocation?.let {
                    MapsManager.isOutsideOfRoute(currentLocation, currentRoute)
                }
            } ?: false
        } ?: false
    }

    private fun showNotification() {
        val title = "Cuidado! Saliste de la ruta"
        val contentMessage =
            "Go to Route ha diseñado una ruta para tu destino. ¿Estás seguro de que quieres salir de ella?"
        NotificationUtils.showNotification(
            applicationContext,
            title,
            contentMessage,
            NotificationOutOfRoute
        )
        notificationShown = true
    }

    interface CurrentLocationStatusProvider {
        fun getCurrentLocation(): Point?
        fun getCurrentRoute(): List<Point>?
    }

}
