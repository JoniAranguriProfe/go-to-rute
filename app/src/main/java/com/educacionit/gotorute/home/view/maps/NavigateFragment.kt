package com.educacionit.gotorute.home.view.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.educacionit.gotorute.R
import com.educacionit.gotorute.ui.components.CustomSearchView
import com.google.android.material.bottomsheet.BottomSheetBehavior


class NavigateFragment : Fragment() {
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
                Toast.makeText(context, locationToSearch, Toast.LENGTH_SHORT).show()
            }
        })
    }
}