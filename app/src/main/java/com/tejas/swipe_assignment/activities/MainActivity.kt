package com.tejas.swipe_assignment.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.tejas.swipe_assignment.NetworkConnectionLiveData
import com.tejas.swipe_assignment.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        networkConnectionLiveData = NetworkConnectionLiveData(application)
        networkConnectionLiveData.observe(this, Observer {
            if(it) showToast("Connected")
            else showToast("Disconnected")
        })
    }
    private fun showToast(message: String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}