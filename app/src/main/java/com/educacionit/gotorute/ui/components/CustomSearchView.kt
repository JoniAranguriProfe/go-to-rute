package com.educacionit.gotorute.ui.components

import SearchAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educacionit.gotorute.R
import com.educacionit.gotorute.home.model.maps.Place


class CustomSearchView(context: Context, @Nullable attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    private lateinit var searchInput: EditText
    private lateinit var searchResultRecycler: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.custom_search_view, this)
        searchInput = findViewById(R.id.search_input)
        searchResultRecycler = findViewById(R.id.search_result_recycler)
        searchInput.clearFocus()

        searchResultRecycler.layoutManager = LinearLayoutManager(context)
        searchAdapter = SearchAdapter(context)
        searchResultRecycler.adapter = searchAdapter
    }

    fun setOnSearchLocationListener(searchLocationListener: SearchLocationListener) {
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                searchLocationListener.performSearch(v.text.toString())
                dismissKeyboard(searchInput)
            }

            return@setOnEditorActionListener false
        }
    }

    fun setOnPlaceClickListener(onPlaceClickListener: SearchAdapter.OnPlaceClickListener) {
        searchAdapter.setOnPlaceClickListener(onPlaceClickListener)
    }

    private fun dismissKeyboard(focusedView: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }

    fun showSearchResults(results: List<Place>?) {
        searchAdapter.setItems(results ?: emptyList())
    }

    interface SearchLocationListener {
        fun performSearch(locationToSearch: String)
    }
}
