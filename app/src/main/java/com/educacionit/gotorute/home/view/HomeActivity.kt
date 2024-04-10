package com.educacionit.gotorute.home.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.educacionit.gotorute.R
import com.educacionit.gotorute.home.view.favorites.MyRoutesFragment
import com.educacionit.gotorute.home.view.maps.NavigateFragment
import com.educacionit.gotorute.home.view.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var navigateFragment: Fragment
    private lateinit var profileFragment: Fragment
    private lateinit var myRoutesFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        configureViews()
    }

    private fun configureViews() {
        navigateFragment = NavigateFragment()
        profileFragment = ProfileFragment()
        myRoutesFragment = MyRoutesFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_my_routes -> {
                    loadFragment(myRoutesFragment)
                    true
                }

                R.id.menu_navigate -> {
                    loadFragment(navigateFragment)
                    true
                }

                R.id.menu_profile -> {
                    loadFragment(profileFragment)
                    true
                }

                else -> false
            }
        }
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_container, fragment)
        transaction.commit()
    }
}