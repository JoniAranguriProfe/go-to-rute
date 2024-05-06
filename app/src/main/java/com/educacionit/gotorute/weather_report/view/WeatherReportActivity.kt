package com.educacionit.gotorute.weather_report.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.educacionit.gotorute.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class WeatherReportActivity : AppCompatActivity() {

    private lateinit var weatherWebview: WebView
    private lateinit var loadingView: CircularProgressIndicator
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_report)

        weatherWebview = findViewById(R.id.weather_webview)
        loadingView = findViewById(R.id.loading_view)
        toolbar = findViewById(R.id.weather_toolbar)
        configureViews()
    }

    private fun configureViews() {
        setSupportActionBar(toolbar)
        loadingView.visibility = View.GONE
        configureWebview()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebview() {
        weatherWebview.settings.javaScriptEnabled = true
        weatherWebview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingView.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingView.visibility = View.GONE
            }
        }
        loadCustomUrl()
    }

    private fun loadCustomUrl() {
        val latitude = intent.getStringExtra(LATITUDE_EXTRA)
        val longitude = intent.getStringExtra(LONGITUDE_EXTRA)
        val weatherUrl= "https://windy.app/map/#c=$latitude,$longitude&z=30"
        weatherWebview.loadUrl(weatherUrl)
    }

    companion object{
        const val LATITUDE_EXTRA = "LATITUDE_EXTRA"
        const val LONGITUDE_EXTRA = "LONGITUDE_EXTRA"
    }
}