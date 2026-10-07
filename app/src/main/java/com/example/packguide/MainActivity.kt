package com.example.packguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PackGuideTheme {
                MainScreen()
            }
        }
    }
}
