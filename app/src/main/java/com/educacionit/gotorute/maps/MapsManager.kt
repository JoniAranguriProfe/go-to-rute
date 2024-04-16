package com.educacionit.gotorute.maps

import android.content.Context
import com.educacionit.gotorute.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object MapsManager {
    private var currentRoute: Polyline? = null
    private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
        // Convert latitude and longitude to radians
        val lat1 = latLng1.latitude * PI / 180.0
        val long1 = latLng1.longitude * PI / 180.0
        val lat2 = latLng2.latitude * PI / 180.0
        val long2 = latLng2.longitude * PI / 180.0

        // Calculate difference in longitude
        val dLon = (long2 - long1)

        // Calculate components of the bearing formula
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)

        // Calculate bearing using atan2
        var brng = atan2(y, x)
        brng = Math.toDegrees(brng)

        // Ensure bearing is within range 0 to 360 degrees
        brng = (brng + 360) % 360

        return brng
    }

    fun addRouteToMap(safeContext: Context, googleMap: GoogleMap, route: List<LatLng>) {
        currentRoute?.remove()
        val polylineOptions = PolylineOptions()
            .addAll(route)
            .color(safeContext.getColor(R.color.violet_primary))
            .width(30f)
        currentRoute = googleMap.addPolyline(polylineOptions)
    }

    fun alignMapToRoute(googleMap: GoogleMap, route: List<LatLng>) {
        val bearing = if (route.size >= 2) {
            val startPoint = route[0]
            val endPoint = route[1]
            bearingBetweenLocations(startPoint, endPoint).toFloat()
        } else 0f

        // Create a camera update with the desired bearing
        val cameraPosition = CameraPosition.Builder()
            .target(route[0]) // Set the center of the map to the starting point of the route
            .zoom(18f) // Set a desired zoom level
            .bearing(bearing) // Set the desired bearing (orientation) of the camera
            .build()

        // Animate the camera to the specified position
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun centerMapIntoLocation(googleMap: GoogleMap, initialPoint: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(initialPoint) // Sets the center of the map to Mountain View
            .zoom(17f)            // Sets the zoom
            .bearing(90f)         // Sets the orientation of the camera to east
            .tilt(30f)            // Sets the tilt of the camera to 30 degrees
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null)
    }

    fun addMarkerToMap(googleMap: GoogleMap, initialPoint: LatLng, title: String = "No title") {
        googleMap.addMarker(
            MarkerOptions()
                .position(initialPoint)
                .title(title)
        )
    }
}