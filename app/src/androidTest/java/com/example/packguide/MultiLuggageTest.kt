package com.example.packguide

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.activity.ComponentActivity
import android.os.Build
import android.view.WindowManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultiLuggageTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    @Test fun measurementsSelectionControlsCatalogDestinationsAndPackingGuide() {
        compose.runOnUiThread {
            compose.activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true)
                compose.activity.setTurnScreenOn(true)
            }
        }
        compose.setContent {
            PackGuideTheme {
                MainScreen(AppSettings(), onThemeChange = {}, onLanguageChange = {}, onLogout = {})
            }
        }
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText("MEDIDAS DEL EQUIPAJE")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
        compose.onNodeWithText("MANO").performClick()
        compose.onNodeWithText("BODEGA").performClick()
        compose.onNodeWithText("CABINA").performClick()
        compose.onNodeWithText("QUITAR MALETA").performClick()
        repeat(2) {
            compose.onNode(hasScrollAction()).performScrollToNode(hasText("CONFIRMAR MEDIDAS"))
            compose.onAllNodesWithText("CONFIRMAR MEDIDAS")[0].performClick()
        }
        compose.onNodeWithText("CATÁLOGO").performClick()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: MANO")[0].performScrollTo().performClick()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: MANO")[0].performScrollTo().performClick()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: BODEGA")[0].performScrollTo().performClick()
        compose.onNodeWithText("TOTAL: 3 unidades · 3.6 L").assertExists()
        compose.onNodeWithText("GUÍA").performClick()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("0 de 2 unidades empacadas · 0%"))
        compose.onNodeWithText("0 de 2 unidades empacadas · 0%").assertIsDisplayed()
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("0 de 1 unidad empacada · 0%"))
        compose.onNodeWithText("0 de 1 unidad empacada · 0%").assertIsDisplayed()
        compose.onNodeWithText("RESUMEN").performClick()
        compose.onNodeWithText("TOTAL: 3 unidades · 0 empacadas").assertExists()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
    }
}
