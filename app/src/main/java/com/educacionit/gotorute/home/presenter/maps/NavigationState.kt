package com.educacionit.gotorute.home.presenter.maps

import com.google.android.gms.maps.model.LatLng

sealed class NavigationState
data class NavigationStarted(val route: List<LatLng>, val destinationName: String) : NavigationState()
data object NotNavigating : NavigationState()