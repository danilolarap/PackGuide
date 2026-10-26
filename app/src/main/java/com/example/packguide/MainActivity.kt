package com.example.packguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.packguide.data.auth.AuthRepository
import com.example.packguide.data.auth.PackGuideDatabase
import com.example.packguide.ui.screens.AuthScreen
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme

class MainActivity : ComponentActivity() {
    private val authRepository by lazy {
        AuthRepository(PackGuideDatabase.getInstance(applicationContext).userAccountDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PackGuideTheme {
                PackGuideRoot(authRepository)
            }
        }
    }
}

@Composable
private fun PackGuideRoot(authRepository: AuthRepository) {
    var isAuthenticated by remember { mutableStateOf(false) }
    if (isAuthenticated) MainScreen()
    else AuthScreen(authRepository) { isAuthenticated = true }
}
