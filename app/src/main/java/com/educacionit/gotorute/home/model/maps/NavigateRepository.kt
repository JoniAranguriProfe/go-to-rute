package com.educacionit.gotorute.home.model.maps

import com.educacionit.gotorute.contract.NavigateContract

class NavigateRepository : NavigateContract.NavigateModel {
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

    override fun getCurrentPointPosition(): Point {
        // Todo: Get current position from LocationService
        return Point(-34.679437, -58.553777)
    }
}