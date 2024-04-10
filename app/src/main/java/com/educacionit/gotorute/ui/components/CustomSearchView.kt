package com.educacionit.gotorute.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.educacionit.gotorute.R


class CustomSearchView(context: Context, @Nullable attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    private lateinit var searchInput: EditText
    private lateinit var searchResultRecycler: RecyclerView

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.custom_search_view, this)
        searchInput = findViewById(R.id.search_input)
        searchResultRecycler = findViewById(R.id.search_result_recycler)
        searchInput.clearFocus()
    }

    fun setOnSearchLocationListener(searchLocationListener: SearchLocationListener) {
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                searchLocationListener.performSearch(v.text.toString())
            }
            return@setOnEditorActionListener false
        }
    }

    fun showSearchResults(results: List<String?>?) {
        // Display search results in RecyclerView
        // Create and set adapter for search results
    }

    interface SearchLocationListener {
        fun performSearch(locationToSearch: String)
    }
}
