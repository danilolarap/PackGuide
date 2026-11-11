package com.example.packguide.ui.screens


import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.Box
import com.example.packguide.PackGuideSessionViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.navigation.TechBottomNavBar
import com.example.packguide.settings.AppLanguage
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.components.SettingsDialog

@Composable
fun MainScreen(
    settings: AppSettings,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onLogout: () -> Unit
) {
    val session: PackGuideSessionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val currentTab = session.currentTab
    BackHandler(enabled = session.canGoBack) { session.goBack() }
    var showSettings by remember { mutableStateOf(false) }
    val packing = session.packing
    val preview: com.example.packguide.ai.PackingPreviewViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    androidx.compose.runtime.LaunchedEffect(packing.selectedLuggage) { preview.retain(packing.selectedLuggage) }
    // Summary photos and previews survive Activity recreation.

    fun navigateTo(tab: NavTab) {
        session.navigateTo(tab)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PackGuideColors.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (packing.allDimensionsConfirmed) {
                TechBottomNavBar(
                    modifier = Modifier.testTag("main-navigation"),
                    selectedTab = currentTab,
                    onTabSelected = ::navigateTo,
                    tabs = NavTab.tabs,
                    currentDestination = currentTab,
                    onSettingsClick = { showSettings = true }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    is NavTab.Measurements -> MeasurementsScreen(packing = packing, onOpenCatalog = { navigateTo(NavTab.Catalog) })
                    is NavTab.Catalog -> CatalogScreen(packing = packing, onCreatePlan = { navigateTo(NavTab.Guide) })
                    is NavTab.Guide -> GuideScreen(packing = packing, onNavigateToTab = ::navigateTo)
                    is NavTab.Summary -> SummaryScreen(

                        packing = packing,
                        preview = preview,
                        onNavigateToTab = ::navigateTo
                    )
                }
            }
            if (showSettings) {
                SettingsDialog(
                    language = settings.language,
                    isDarkTheme = settings.isDarkTheme,
                    showLogout = true,
                    onToggleTheme = { onThemeChange(!settings.isDarkTheme) },
                    onToggleLanguage = {
                        onLanguageChange(if (settings.language == AppLanguage.Spanish) AppLanguage.English else AppLanguage.Spanish)
                    },
                    onLogout = {
                        showSettings = false
                        preview.clear()
                        onLogout()
                    },
                    onDismiss = { showSettings = false }
                )
            }
        }
    }
}
