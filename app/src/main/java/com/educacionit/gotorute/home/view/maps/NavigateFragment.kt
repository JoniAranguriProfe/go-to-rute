package com.educacionit.gotorute.home.view.maps

import SearchAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.educacionit.gotorute.R
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.NavigateContract
import com.educacionit.gotorute.home.model.maps.NavigateRepository
import com.educacionit.gotorute.home.model.maps.Place
import com.educacionit.gotorute.home.presenter.maps.NavigatePresenter
import com.educacionit.gotorute.maps.MapsManager
import com.educacionit.gotorute.ui.components.CustomSearchView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior

class NavigateFragment : Fragment(), OnMapReadyCallback,
    NavigateContract.NavigateView<BaseContract.IBaseView> {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var placesSearchView: CustomSearchView
    private lateinit var navigatePresenter: NavigateContract.IFragmentNavigatePresenter<NavigateContract.NavigateView<BaseContract.IBaseView>>
    private lateinit var googleMap: GoogleMap
    private var locationState: LocationState = LocationNotGranted

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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placesSearchView.setOnSearchLocationListener(object :
            CustomSearchView.SearchLocationListener {
            override fun performSearch(locationToSearch: String) {
                navigatePresenter.performPlacesSearch(locationToSearch)
            }
        })
        placesSearchView.setOnPlaceClickListener(object : SearchAdapter.OnPlaceClickListener {
            override fun onPlaceClick(destinationPlace: Place) {
                navigatePresenter.getRouteToPlace(destinationPlace)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        })
        initPresenter()
        initMap()
    }

    override fun initPresenter() {
        val navigateModel = NavigateRepository()
        navigatePresenter = NavigatePresenter(navigateModel)
        navigatePresenter.attachView(this)
    }

    private fun initMap() {
        val mapsSupportFragment =
            childFragmentManager.findFragmentById(R.id.location_maps_fragment) as SupportMapFragment
        mapsSupportFragment.getMapAsync(this)
    }

    override fun showSearchResults(placeResults: List<Place>) {
        placesSearchView.showSearchResults(placeResults)
    }

    override fun drawRoute(route: List<LatLng>) {
        context?.let { safeContext ->
            MapsManager.addRouteToMap(safeContext, googleMap, route)
            MapsManager.alignMapToRoute(googleMap, route)
            MapsManager.addMarkerToMap(googleMap, route.last())
        }
    }

    override fun onMapReady(updatedMap: GoogleMap) {
        googleMap = updatedMap
        checkLocationsPermissions()
    }

    private fun checkLocationsPermissions() {
        context?.let { safeContext ->
            if (!hasLocationPermission(safeContext)) {
                requestLocationPermissions()
                return@let
            }
            locationState = LocationGranted
            configureMap()
        }
    }

    private fun requestLocationPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults.first() == PackageManager.PERMISSION_GRANTED
                ) {
                    locationState = LocationGranted
                    configureMap()
                    return
                }
                getParentView().showErrorMessage(getString(R.string.permission_denied_message))
                openAppSettings()
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    @SuppressLint("MissingPermission")
    private fun configureMap() {
        executeWithLocationPermission {
            val initialFakePoint = LatLng(-34.679437, -58.553777)
            MapsManager.addMarkerToMap(googleMap, initialFakePoint)
            MapsManager.centerMapIntoLocation(googleMap, initialFakePoint)

            googleMap.isMyLocationEnabled = true
        }
    }

    private fun hasLocationPermission(safeContext: Context) =
        ActivityCompat.checkSelfPermission(
            safeContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun getParentView(): BaseContract.IBaseView {
        return activity as BaseContract.IBaseView
    }

    private fun executeWithLocationPermission(mapFunction: () -> Unit) {
        if (locationState != LocationGranted) {
            getParentView().showErrorMessage("")
            return
        }
        mapFunction()
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 174
    }
}
