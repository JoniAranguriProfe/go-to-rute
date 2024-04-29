package com.educacionit.gotorute.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.educacionit.gotorute.R
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.home.model.maps.NavigateRepository.Companion.ACTION_NAVIGATION_ALARM
import com.educacionit.gotorute.home.view.favorites.MyRoutesFragment
import com.educacionit.gotorute.home.view.maps.NavigateFragment
import com.educacionit.gotorute.home.view.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeActivity : AppCompatActivity(), BaseContract.IBaseView {

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
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == ACTION_NAVIGATION_ALARM) {
            showDialogInHomeActivity(intent)
        }
    }

    private fun showDialogInHomeActivity(intent: Intent) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Sigues ahí?")
            .setMessage("Ha pasado un tiempo y aún no has llegado al destino..")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun configureViews() {
        navigateFragment = NavigateFragment()
        profileFragment = ProfileFragment()
        myRoutesFragment = MyRoutesFragment()
        loadFragment(navigateFragment)

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
        bottomNavigationView.selectedItemId = R.id.menu_navigate
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_container, fragment)
        transaction.commit()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getViewContext(): Context = this

    override fun initPresenter() {
        // Not used
    }
}