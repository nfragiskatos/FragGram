package com.nfragiskatos.fraggram.activities.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nfragiskatos.fraggram.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_add,
                R.id.navigation_favorite,
                R.id.navigation_profile
            )
        )

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.accountSettingsFragment, R.id.signInFragment, R.id.signUpFragment -> {
                    navView.visibility = View.GONE
                } else -> navView.visibility = View.VISIBLE
            }
        }
        navView.setupWithNavController(navController)
    }
}