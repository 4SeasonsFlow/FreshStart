package com.example.freshstart

import android.app.Application
import com.example.freshstart.data.AppContainer
import com.example.freshstart.data.DefaultAppContainer

class FreshStartApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}