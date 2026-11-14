package com.example.smart_bus_tracker_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.smart_bus_tracker_app.navigation.AppNavGraph
import com.example.smart_bus_tracker_app.ui.theme.SmartbustrackerappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartbustrackerappTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}