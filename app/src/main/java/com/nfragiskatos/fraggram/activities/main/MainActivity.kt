package com.nfragiskatos.fraggram.activities.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nfragiskatos.fraggram.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // Per
        // https://stackoverflow.com/questions/59275009/fragmentcontainerview-using-findnavcontroller/59275182#59275182
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_test) as NavHostFragment
        val navController = navHostFragment.navController

        // If user is signed in, start at home screen, else start at log in screen.
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.navigation)
        graph.startDestination =
            if (Firebase.auth.currentUser == null) R.id.signInFragment else R.id.navigation_home
        navController.graph = graph

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.accountSettingsFragment, R.id.signInFragment, R.id.signUpFragment -> {
                    navView.visibility = View.GONE
                }
                else -> navView.visibility = View.VISIBLE
            }
        }
        navView.setupWithNavController(navController)
    }
}