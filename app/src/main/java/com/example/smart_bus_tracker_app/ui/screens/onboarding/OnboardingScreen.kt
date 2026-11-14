package com.example.smart_bus_tracker_app.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_bus_tracker_app.data.model.onboardingPages
import com.example.smart_bus_tracker_app.ui.screens.onboarding.components.OnboardingDotIndicator
import com.example.smart_bus_tracker_app.ui.screens.onboarding.components.OnboardingPageItem
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit = {},
    onGetStartedClick: () -> Unit = onFinishOnboarding,
    onLoginClick: () -> Unit = {},
    isDarkMode: Boolean = isSystemInDarkTheme()
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingPages.size - 1

    val backgroundColor = if (isDarkMode) Color(0xFF0F172A) else Color(0xFFFFFFFF)
    val skipTextColor = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar with Skip Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = !isLastPage,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(onboardingPages.size - 1)
                        }
                    }
                ) {
                    Text(
                        text = "Skip",
                        color = skipTextColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Horizontal Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            OnboardingPageItem(
                page = onboardingPages[pageIndex],
                isDarkMode = isDarkMode
            )
        }

        // Bottom Navigation Area (Dot Indicator + Full-width Gradient Button)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Dot Indicator
            OnboardingDotIndicator(
                totalDots = onboardingPages.size,
                selectedIndex = pagerState.currentPage,
                isDarkMode = isDarkMode,
                onDotClick = { targetIndex ->
                    scope.launch {
                        pagerState.animateScrollToPage(targetIndex)
                    }
                },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Full-width Gradient Button
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF3B82F6),
                    Color(0xFF1D4ED8)
                )
            )

            Button(
                onClick = {
                    if (isLastPage) {
                        onGetStartedClick()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(
                        brush = gradientBrush,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isLastPage) "Get Started" else "Next",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
