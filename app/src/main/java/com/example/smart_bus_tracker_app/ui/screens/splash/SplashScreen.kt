package com.example.smart_bus_tracker_app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.smart_bus_tracker_app.R
import kotlinx.coroutines.delay

val NextHaltBlue = Color(0xFF0266F4)

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        delay(2500L)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NextHaltBlue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bus_tracker_logo),
            contentDescription = "NextHalt App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}