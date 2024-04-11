package com.educacionit.gotorute.home.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.educacionit.gotorute.R
import com.educacionit.gotorute.ui.components.CustomSearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior


class NavigateFragment : Fragment(), OnMapReadyCallback {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var placesSearchView: CustomSearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigate, container, false)

        val bottomSheet = view.findViewById<View>(R.id.standard_bottom_sheet)
        placesSearchView = view.findViewById(R.id.places_search_view)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.peekHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_peek_height)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placesSearchView.setOnSearchLocationListener(object :
            CustomSearchView.SearchLocationListener {
            override fun performSearch(locationToSearch: String) {
                showSearchResults(locationToSearch)
            }
        })
        configureMap()
    }

    private fun configureMap() {
        val mapsSupportFragment =
            childFragmentManager.findFragmentById(R.id.location_maps_fragment) as SupportMapFragment
        mapsSupportFragment.getMapAsync(this)
    }

    fun showSearchResults(locationToSearch: String) {
        placesSearchView.showSearchResults(
            listOf(
                locationToSearch,
                locationToSearch,
                locationToSearch
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val abasto = LatLng(-34.6037283, -58.4125926)
        googleMap.addMarker(
            MarkerOptions()
                .position(abasto)
                .title("Marker in Abasto")
        )
        val cameraPosition = CameraPosition.Builder()
            .target(abasto) // Sets the center of the map to Mountain View
            .zoom(17f)            // Sets the zoom
            .bearing(90f)         // Sets the orientation of the camera to east
            .tilt(30f)            // Sets the tilt of the camera to 30 degrees
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null)
    }
}