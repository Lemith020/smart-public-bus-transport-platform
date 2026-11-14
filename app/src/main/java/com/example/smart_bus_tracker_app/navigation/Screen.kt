package com.example.smart_bus_tracker_app.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding")
    object RoleSelection : Screen("role_selection_screen")
    object Login : Screen("login_screen")
    object PassengerHome : Screen("passenger_home")
    object DriverHome : Screen("driver_home")
}