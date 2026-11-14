package com.example.smart_bus_tracker_app.data.model

import androidx.annotation.DrawableRes
import com.example.smart_bus_tracker_app.R

data class OnboardingPageData(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_tracking,
        title = "Track Your Bus Live",
        description = "See real-time bus locations on the map, current halts, and accurate distance estimation."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_alerts,
        title = "Know When It Arrives",
        description = "Get precise arrival times and instant alerts before your bus reaches your halt."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_tracking,
        title = "Send & Track Parcels",
        description = "Dispatch packages safely and quickly via buses on your route, with live delivery tracking."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_alerts,
        title = "Passengers & Drivers Connected",
        description = "Notify drivers that you are waiting at a halt, while drivers monitor passenger demand."
    )
)
