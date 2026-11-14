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
