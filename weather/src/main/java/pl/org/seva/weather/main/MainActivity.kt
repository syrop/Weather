package pl.org.seva.weather.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.act_main.*
import pl.org.seva.weather.R

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.act_main)
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onStart() {
        super.onStart()
        println("wiktor on start")
    }

    override fun onStop() {
        super.onStop()
        println("wiktor on stop")
    }

    override fun onResume() {
        super.onResume()
        println("wiktor on resume")
    }

    override fun onPause() {
        super.onPause()
        println("wiktor on pause")
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
