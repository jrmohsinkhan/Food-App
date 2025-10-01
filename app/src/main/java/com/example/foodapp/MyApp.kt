package com.example.foodapp

import android.app.Application
import com.example.foodapp.config.ApiClient

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize ApiClient with application context
        ApiClient.init(this)
    }
}
