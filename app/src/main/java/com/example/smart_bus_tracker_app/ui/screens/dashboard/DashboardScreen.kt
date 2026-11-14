package com.example.smart_bus_tracker_app.ui.screens.dashboard

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_bus_tracker_app.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Supported Languages
enum class AppLanguage(val code: String, val label: String, val flag: String) {
    SINHALA("si", "සිංහල", "🇱🇰"),
    ENGLISH("en", "English", "🇬🇧"),
    TAMIL("ta", "தமிழ்", "🇱🇰")
}

// Route Option Model
data class RouteOption(
    val id: String,
    val titleSi: String,
    val titleEn: String,
    val titleTa: String
)

// Dashboard Card Item Model
data class DashboardCardItem(
    val id: String,
    val titleSi: String,
    val titleEn: String,
    val titleTa: String,
    val subtitleSi: String,
    val subtitleEn: String,
    val subtitleTa: String,
    val icon: ImageVector,
    val accentColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onCardClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var currentLanguage by remember { mutableStateOf(AppLanguage.SINHALA) }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    var isRouteMenuExpanded by remember { mutableStateOf(false) }

    // Route Options
    val routes = remember {
        listOf(
            RouteOption("r1", "ප්‍රධාන මාර්ගය (කොළඹ - මහනුවර)", "Main Line (Colombo - Kandy)", "பிரதான பாதை (கொழும்பு - கண்டி)"),
            RouteOption("r2", "දකුණු අධිවේගී මාර්ගය (මාතර)", "Southern Expressway (Matara)", "தெற்கு அதிவேக பாதை (மாத்தறை)"),
            RouteOption("r3", "මුහුදුබඩ මාර්ගය (කොළඹ - ගාල්ල)", "Coastal Line (Colombo - Galle)", "கடற்கரை பாதை (கொழும்பு - காலி)"),
            RouteOption("r4", "උතුරු මාර්ගය (කොළඹ - යාපනය)", "Northern Line (Colombo - Jaffna)", "வடக்கு பாதை (கொழும்பு - யாழ்ப்பாணம்)")
        )
    }
    var selectedRoute by remember { mutableStateOf(routes[0]) }

    // Clock State
    var currentTimeString by remember { mutableStateOf("") }
    var currentDateString by remember { mutableStateOf("") }

    // Live clock ticker formatted to match reference image (e.g. "ප.ව. 09:28" and "සෙනසුරාදා 2026.11.14")
    LaunchedEffect(currentLanguage) {
        while (true) {
            val locale = Locale.forLanguageTag(currentLanguage.code)
            val now = Date()
            if (currentLanguage == AppLanguage.SINHALA) {
                val isPm = SimpleDateFormat("a", Locale.US).format(now).uppercase() == "PM"
                val amPmStr = if (isPm) "ප.ව." else "පෙ.ව."
                val timeDigits = SimpleDateFormat("hh:mm", Locale.US).format(now)
                currentTimeString = "$amPmStr $timeDigits"

                val dayName = SimpleDateFormat("EEEE", locale).format(now)
                val dateDigits = SimpleDateFormat("yyyy.MM.dd", Locale.US).format(now)
                currentDateString = "$dayName $dateDigits"
            } else {
                val timeFormat = SimpleDateFormat("hh:mm a", locale)
                val dateFormat = SimpleDateFormat("EEEE yyyy.MM.dd", locale)
                currentTimeString = timeFormat.format(now)
                currentDateString = dateFormat.format(now)
            }
            delay(1000L)
        }
    }

    // Dashboard Cards List
    val cards = remember {
        listOf(
            DashboardCardItem(
                id = "live_tracking",
                titleSi = "සජීවී බස් ලුහුබැඳීම",
                titleEn = "Live Bus Tracking",
                titleTa = "நேரடி பேருந்து கண்காணிப்பு",
                subtitleSi = "GPS සහ පැමිණීමේ වේලාව",
                subtitleEn = "Real-time GPS & ETA",
                subtitleTa = "நேரலை ஜிபிஎஸ் & வருகை",
                icon = Icons.Default.LocationOn,
                accentColor = Color(0xFF00E5FF)
            ),
            DashboardCardItem(
                id = "waiting_board",
                titleSi = "මගී රැඳී සිටින මධ්‍යස්ථාන",
                titleEn = "Nearby Bus Halts",
                titleTa = "காத்திருக்கும் பஸ் நிறுத்தம்",
                subtitleSi = "නැවතුම්පොළ සහ මගී තත්ත්වය",
                subtitleEn = "Halts & Passenger Status",
                subtitleTa = "பஸ் நிறுத்த நிலை",
                icon = Icons.Default.DirectionsBus,
                accentColor = Color(0xFFFF1744)
            ),
            DashboardCardItem(
                id = "police_alerts",
                titleSi = "පොලිස් සහ හදිසි දැනුම්දීම්",
                titleEn = "Police & Incidents",
                titleTa = "காவல் & அவசர எச்சரிக்கைகள்",
                subtitleSi = "මාර්ග සහ ආරක්ෂක ඇලර්ට්",
                subtitleEn = "Traffic & Safety Notices",
                subtitleTa = "போக்குவரத்து & பாதுகாப்பு",
                icon = Icons.Default.Shield,
                accentColor = Color(0xFFFFB300)
            ),
            DashboardCardItem(
                id = "parcel_service",
                titleSi = "පාර්සල් සේවාව",
                titleEn = "Parcel Cargo Service",
                titleTa = "பார்சல் சேவை",
                subtitleSi = "පාර්සල් යැවීම සහ පරීක්ෂාව",
                subtitleEn = "Send & Track Packages",
                subtitleTa = "பார்சல் கண்காணிப்பு",
                icon = Icons.Default.LocalShipping,
                accentColor = Color(0xFF00E676)
            ),
            DashboardCardItem(
                id = "seat_status",
                titleSi = "අසුන්වල තත්ත්වය",
                titleEn = "Seat Occupancy",
                titleTa = "இருக்கை நிலை",
                subtitleSi = "සජීවී අසුන් ප්‍රමාණය",
                subtitleEn = "Live Seat Availability",
                subtitleTa = "இருக்கை நிலைமை",
                icon = Icons.Default.EventSeat,
                accentColor = Color(0xFFE040FB)
            ),
            DashboardCardItem(
                id = "feedback_ratings",
                titleSi = "ප්‍රතිචාර සහ ශ්‍රේණිගත කිරීම්",
                titleEn = "Ratings & Reviews",
                titleTa = "கருத்து & மதிப்பீடுகள்",
                subtitleSi = "සේවා සමාලෝචන",
                subtitleEn = "Service Reviews & Star Rate",
                subtitleTa = "சேவை மதிப்பீடு",
                icon = Icons.Default.Star,
                accentColor = Color(0xFFFF9100)
            )
        )
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->

        // Blue Gradient Background Container covering full screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A), // Top Dark Slate Navy
                            Color(0xFF0B1E38), // Rich Deep Blue
                            Color(0xFF07172E), // Electric Navy Blue
                            Color(0xFF030E1F)  // Dark Blue Base
                        )
                    )
                )
        ) {
            // Smooth Scrollable Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // Full Bleed Top Banner Header (Edge-to-Edge)
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    ) {
                        // Background Banner Image
                        Image(
                            painter = painterResource(id = R.drawable.banner),
                            contentDescription = "NextHalt Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Smooth Dark Gradient Overlay (Scrim) Blending into Blue Background & Enhancing Text Readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.85f),
                                            Color.Black.copy(alpha = 0.40f),
                                            Color(0xFF0F172A)
                                        )
                                    )
                                )
                        )

                        // Embedded Header Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Top Row: Title/Subtitle (Left) + White Language Pill & Crisp Time/Date (Right - Matching Image)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                // App Title & Subtitle
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "NextHalt",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFFD700)) // Yellow Accent Dot
                                        )
                                    }

                                    Text(
                                        text = when (currentLanguage) {
                                            AppLanguage.SINHALA -> "වේගවත් ගමනකට"
                                            AppLanguage.ENGLISH -> "Smart & Fast Transit"
                                            AppLanguage.TAMIL -> "விரைவான பயணத்திற்கு"
                                        },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFFFFD700), // Yellow Subtitle Accent
                                        letterSpacing = 0.3.sp
                                    )
                                }

                                // Right Side: White Language Switcher Pill & Crisp Time/Date (Matching Reference Image)
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    // Sleek White Language Button (A / අ Translate Card Style)
                                    Box {
                                        Surface(
                                            onClick = { isLanguageMenuExpanded = true },
                                            shape = RoundedCornerShape(10.dp),
                                            color = Color(0xEEFFFFFF), // Crisp White Background
                                            border = BorderStroke(1.dp, Color(0x33000000)),
                                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(10.dp))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Translate,
                                                    contentDescription = "Language Switcher",
                                                    tint = Color(0xFF0F172A),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = currentLanguage.code.uppercase(),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF0F172A)
                                                )
                                            }
                                        }

                                        DropdownMenu(
                                            expanded = isLanguageMenuExpanded,
                                            onDismissRequest = { isLanguageMenuExpanded = false },
                                            modifier = Modifier.background(Color(0xFF1E293B))
                                        ) {
                                            AppLanguage.entries.forEach { lang ->
                                                DropdownMenuItem(
                                                    text = {
                                                        Text(
                                                            text = "${lang.flag}  ${lang.label}",
                                                            color = if (lang == currentLanguage) Color(0xFF00E5FF) else Color.White,
                                                            fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                                        )
                                                    },
                                                    onClick = {
                                                        currentLanguage = lang
                                                        isLanguageMenuExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Crisp White Sinhala Time (e.g. "ප.ව. 09:28") with High Contrast Shadow
                                    Text(
                                        text = currentTimeString.ifEmpty { "ප.ව. 09:28" },
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        style = TextStyle(
                                            shadow = Shadow(
                                                color = Color.Black.copy(alpha = 0.9f),
                                                blurRadius = 8f
                                            )
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Crisp White Sinhala Date (e.g. "සෙනසුරාදා 2026.11.14") with High Contrast Shadow
                                    Text(
                                        text = currentDateString.ifEmpty { "සෙනසුරාදා 2026.11.14" },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White.copy(alpha = 0.95f),
                                        style = TextStyle(
                                            shadow = Shadow(
                                                color = Color.Black.copy(alpha = 0.9f),
                                                blurRadius = 6f
                                            )
                                        )
                                    )
                                }
                            }

                            // Bottom Banner Section: Route Selection Dropdown Pill
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Surface(
                                    onClick = { isRouteMenuExpanded = true },
                                    shape = RoundedCornerShape(24.dp),
                                    color = Color(0xDD0F172A),
                                    border = BorderStroke(1.2.dp, Color(0xFFFFD700).copy(alpha = 0.85f)),
                                    modifier = Modifier.shadow(10.dp, RoundedCornerShape(24.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f, fill = false)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFFD700))
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = when (currentLanguage) {
                                                    AppLanguage.SINHALA -> selectedRoute.titleSi
                                                    AppLanguage.ENGLISH -> selectedRoute.titleEn
                                                    AppLanguage.TAMIL -> selectedRoute.titleTa
                                                },
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select Route",
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = isRouteMenuExpanded,
                                    onDismissRequest = { isRouteMenuExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .background(Color(0xFF1E293B))
                                ) {
                                    routes.forEach { route ->
                                        val title = when (currentLanguage) {
                                            AppLanguage.SINHALA -> route.titleSi
                                            AppLanguage.ENGLISH -> route.titleEn
                                            AppLanguage.TAMIL -> route.titleTa
                                        }
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = title,
                                                    color = if (route == selectedRoute) Color(0xFFFFD700) else Color.White,
                                                    fontWeight = if (route == selectedRoute) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                selectedRoute = route
                                                isRouteMenuExpanded = false
                                                Toast.makeText(context, title, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Section Header Title
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(18.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color(0xFF00E5FF))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.SINHALA -> "ප්‍රධාන සේවාවන්"
                                AppLanguage.ENGLISH -> "Main Services"
                                AppLanguage.TAMIL -> "முக்கிய சேவைகள்"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Prominent Feature Grid Cards (Padded Grid Items)
                items(cards) { item ->
                    Box(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        GlassmorphismDashboardCard(
                            item = item,
                            language = currentLanguage,
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Navigating to ${item.titleEn}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onCardClick(item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlassmorphismDashboardCard(
    item: DashboardCardItem,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val title = when (language) {
        AppLanguage.SINHALA -> item.titleSi
        AppLanguage.ENGLISH -> item.titleEn
        AppLanguage.TAMIL -> item.titleTa
    }

    val subtitle = when (language) {
        AppLanguage.SINHALA -> item.subtitleSi
        AppLanguage.ENGLISH -> item.subtitleEn
        AppLanguage.TAMIL -> item.subtitleTa
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(165.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = item.accentColor.copy(alpha = 0.35f),
                spotColor = item.accentColor.copy(alpha = 0.5f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xDD1E293B) // Dark Translucent Glassmorphism background
        ),
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.15f)) // Translucent glass stroke
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            item.accentColor.copy(alpha = 0.22f), // Glowing top tint
                            Color.Transparent
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Circle Glow Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = item.accentColor.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, item.accentColor.copy(alpha = 0.45f))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = title,
                                tint = item.accentColor,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Indicator Accent Line
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(item.accentColor.copy(alpha = 0.8f))
                    )
                }

                // Title & Subtitle Section
                Column {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1), // Crisp light text for glass readability
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
