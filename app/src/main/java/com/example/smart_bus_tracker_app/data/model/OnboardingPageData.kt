package com.example.smart_bus_tracker_app.data.model

import androidx.annotation.DrawableRes
import com.example.smart_bus_tracker_app.R

data class OnboardingPageData(
    @param:DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_tracking,
        title = "Track your bus live",
        description = "See where your bus is on the map, which halt it is at, and how far it is from you."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_alerts,
        title = "Know when it arrives",
        description = "Get arrival times and alerts before your bus reaches your halt, so you never wait too long."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_parcel,
        title = "Send & track parcels",
        description = "Send your packages safely and quickly via buses on your route, and track their delivery status live."
    ),
    OnboardingPageData(
        imageRes = R.drawable.img_onboarding_connected,
        title = "Passengers & drivers, connected",
        description = "Tell the driver you are waiting at a halt. Drivers see who is waiting and where other buses are."
    )
)
