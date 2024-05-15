package com.tejas.swipe_assignment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.tejas.swipe_assignment.NetworkConnectionLiveData
import com.tejas.swipe_assignment.R

class MainActivity : AppCompatActivity() {

    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkConnectionLiveData = NetworkConnectionLiveData(application)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    private fun showToast(message: String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}