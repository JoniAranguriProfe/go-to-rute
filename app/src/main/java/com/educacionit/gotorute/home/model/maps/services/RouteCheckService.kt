package com.educacionit.gotorute.home.model.maps.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationProvider
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.educacionit.gotorute.R
import com.educacionit.gotorute.home.model.maps.Point
import com.educacionit.gotorute.maps.MapsManager
import com.google.android.gms.common.util.MapUtils
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*

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
                    MapsManager.isOutsideOfRoute(currentLocation, currentRoute) }
            } ?: false
        } ?: false
    }

    private fun showNotification() {
        val notification = buildNotification()
        notificationManager?.notify(NOTIFICATION_ID, notification)
        notificationShown = true
    }

    private fun buildNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Cuidado! Saliste de la ruta")
            .setContentText("Go to Route ha diseñado una ruta para tu destino. ¿Estás seguro de que quieres salir de ella?")
            .setSmallIcon(R.drawable.map_arrow_square)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.educacionit_logo))
            .setAutoCancel(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Notifications out of route",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager?.createNotificationChannel(channel)
    }

    interface CurrentLocationStatusProvider {
        fun getCurrentLocation(): Point?
        fun getCurrentRoute(): List<Point>?
    }

    companion object {
        const val CHANNEL_ID = "Notifications out of route"
        const val NOTIFICATION_ID = 23223
    }

}
