package com.example.smart_bus_tracker_app.ui.screens.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_bus_tracker_app.data.model.OnboardingPageData

@Composable
fun OnboardingPageItem(
    page: OnboardingPageData,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    val titleColor = if (isDarkMode) Color.White else Color(0xFF0F172A)
    val bodyColor = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = bodyColor,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
