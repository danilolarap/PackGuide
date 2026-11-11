package com.example.packguide

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import com.example.packguide.settings.AppLanguage
import com.example.packguide.settings.SettingsRepository
import com.example.packguide.ui.navigation.NavTab
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ThemeSessionFlowTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun themeAndLanguageSurviveLogoutWithoutAllowingReturnToSummary() {
        val repository = SettingsRepository(compose.activity)
        runBlocking { repository.setDarkTheme(false); repository.setLanguage(AppLanguage.Spanish) }
        compose.runOnUiThread {
            compose.activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (android.os.Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true); compose.activity.setTurnScreenOn(true)
            }
        }
        compose.runOnIdle {
            val session = ViewModelProvider(compose.activity)[PackGuideSessionViewModel::class.java]
            session.isAuthenticated = true
            session.packing.dimensionsConfirmed.value = true
            session.navigateTo(NavTab.Summary)
        }
        compose.onNodeWithContentDescription("Configuración").performClick()
        compose.onNodeWithText("TEMA").performClick()
        compose.onNodeWithText("IDIOMA").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("SIGN OUT").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("SIGN OUT").performClick()
        compose.waitUntil(10_000) { compose.onAllNodesWithText("SIGN IN").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("SIGN IN").assertIsDisplayed()
        val settings = runBlocking { repository.settings.first() }
        assertTrue(settings.isDarkTheme)
        assertEquals(AppLanguage.English, settings.language)
        compose.runOnIdle {
            val session = ViewModelProvider(compose.activity)[PackGuideSessionViewModel::class.java]
            assertFalse(session.isAuthenticated)
            assertFalse(session.canGoBack)
            assertEquals(NavTab.Measurements, session.currentTab)
        }
    }
}
