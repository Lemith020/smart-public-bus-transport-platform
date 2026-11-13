package com.example.smart_bus_tracker_app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smart_bus_tracker_app.ui.screens.splash.SplashScreen
// import com.example.smart_bus_tracker_app.ui.screens.auth.RoleSelectionScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen එක
        composable(route = Screen.Splash.route) {
            SplashScreen(onNavigateNext = {

                navController.navigate(Screen.RoleSelection.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }


        composable(route = Screen.RoleSelection.route) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Role Selection Screen (Passenger / Driver)")
            }
        }
    }
}