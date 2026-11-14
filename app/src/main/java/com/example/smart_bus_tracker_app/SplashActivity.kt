package com.example.smart_bus_tracker_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {

    private lateinit var layoutStep1: View
    private lateinit var layoutStep2: View
    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView

    private val handler = Handler(Looper.getMainLooper())
    private var activeDotIndex = 0
    private var isAnimatingDots = false

    private val dotRunnable = object : Runnable {
        override fun run() {
            if (!isAnimatingDots) return

            // Cycle through dots: 0 -> 1 -> 2 -> 0
            dot1.setImageResource(if (activeDotIndex == 0) R.drawable.dot_active else R.drawable.dot_inactive)
            dot2.setImageResource(if (activeDotIndex == 1) R.drawable.dot_active else R.drawable.dot_inactive)
            dot3.setImageResource(if (activeDotIndex == 2) R.drawable.dot_active else R.drawable.dot_inactive)

            activeDotIndex = (activeDotIndex + 1) % 3
            handler.postDelayed(this, 444L) // ~3 full cycles across 4000ms
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Immediately remove Android 12+ (API 31+) default system splash screen icon view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        layoutStep1 = findViewById(R.id.layoutStep1)
        layoutStep2 = findViewById(R.id.layoutStep2)
        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)

        startSplashSequence()
    }

    private fun startSplashSequence() {
        // Step 1: Display Step 1 (bushalt background + centered logo) for 2 seconds
        layoutStep1.visibility = View.VISIBLE
        layoutStep2.visibility = View.GONE

        lifecycleScope.launch {
            // Wait 2 seconds (2000ms) for Step 1
            delay(2000L)

            // Step 2: Transition to Step 2 (Bushalt background + brand info + loading dots)
            layoutStep1.visibility = View.GONE
            layoutStep2.visibility = View.VISIBLE

            // Start dot loading animation
            isAnimatingDots = true
            handler.post(dotRunnable)

            // Display Step 2 for 4 seconds (4000ms)
            delay(4000L)

            // Stop dot animation
            isAnimatingDots = false
            handler.removeCallbacks(dotRunnable)

            // Seamlessly transition to MainActivity
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isAnimatingDots = false
        handler.removeCallbacksAndMessages(null)
    }
}
