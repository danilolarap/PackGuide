package com.example.packguide.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.navigation.TechBottomNavBar
import com.example.packguide.ui.theme.BackgroundLight

@Composable
fun MainScreen() {
    var currentTab by remember { mutableStateOf<NavTab?>(NavTab.Scanner) }
    val safeCurrentTab = currentTab ?: NavTab.Scanner

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        bottomBar = {
            TechBottomNavBar(
                selectedTab = safeCurrentTab,
                onTabSelected = { currentTab = it },
                tabs = listOf(NavTab.Scanner, NavTab.Catalog, NavTab.Guide, NavTab.Summary),
                currentDestination = safeCurrentTab
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = safeCurrentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    is NavTab.Scanner -> ScannerScreen()
                    is NavTab.Catalog -> CatalogScreen()
                    is NavTab.Guide -> GuideScreen(onNavigateToTab = { currentTab = it })
                    is NavTab.Summary -> SummaryScreen(onNavigateToTab = { currentTab = it })
                }
            }
        }
    }
}
