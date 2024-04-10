package com.educacionit.gotorute.home.view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.educacionit.gotorute.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        configureViews()
    }

    private fun configureViews() {
        val mainTitle = findViewById<TextView>(R.id.main_title)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_my_routes -> {
                    mainTitle.text = "Estoy en mis rutas"
                    true
                }

                R.id.menu_navigate -> {
                    mainTitle.text = "Estoy en la pantalla del mapa"
                    true
                }

                R.id.menu_profile -> {
                    mainTitle.text = "Estoy en mi perfil"
                    true
                }

                else -> false
            }
        }
    }
}