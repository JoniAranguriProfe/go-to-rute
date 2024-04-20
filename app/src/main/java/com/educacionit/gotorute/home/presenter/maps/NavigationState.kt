package com.educacionit.gotorute.home.presenter.maps

import com.google.android.gms.maps.model.LatLng

sealed class NavigationState
data class NavigationStarted(val route: List<LatLng>) : NavigationState()
data object NotNavigating : NavigationState()