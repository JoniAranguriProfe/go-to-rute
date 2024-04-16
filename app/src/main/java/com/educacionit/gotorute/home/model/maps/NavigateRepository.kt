package com.educacionit.gotorute.home.model.maps

import com.educacionit.gotorute.contract.NavigateContract

class NavigateRepository : NavigateContract.NavigateModel {
    override suspend fun getPlacesFromSearch(placeToSearch: String): List<Place> {
        return ApiServiceProvider.searchServiceAPI.getPlacesFromSearch(placeToSearch = placeToSearch)
            .body()?.map {
            val convertedLatitude = it.latitude.toDouble()
            val convertedLongitude = it.longitude.toDouble()
            Place(
                displayName = it.displayName,
                point = Point(latitude = convertedLatitude, longitude = convertedLongitude)
            )
        } ?: emptyList()
    }

    override fun getRouteToPlace(startPlace: Place, destinationPlace: Place): List<Point> {
        // Todo: Use an API to get routes from two given points
        return (listOf(
            Point(-34.679437, -58.553777),
            Point(
                -34.679217,
                -58.553513
            ),
            Point(
                -34.678996,
                -58.553279
            ),
            Point(
                -34.678119,
                -58.554457
            ),
            Point(
                -34.677234,
                -58.55561
            ),
            Point(
                -34.676409,
                -58.556671
            )
        ))
    }

    override fun getCurrentPointPosition(): Point {
        // Todo: Get current position from LocationService
        return Point(-34.679437, -58.553777)
    }
}