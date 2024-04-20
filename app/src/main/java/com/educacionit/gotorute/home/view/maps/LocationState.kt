package com.educacionit.gotorute.home.view.maps

sealed class LocationState
data object LocationGranted : LocationState()
data object LocationNotGranted : LocationState()