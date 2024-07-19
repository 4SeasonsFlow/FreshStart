package com.example.freshstart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.freshstart.ui.FreshStartApp
import com.example.freshstart.ui.theme.FreshStartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreshStartTheme {
                FreshStartApp()
            }
        }
    }
}
