package com.example.packguide

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultiLuggageTest {
    @get:Rule val compose = createComposeRule()

    @Test fun scannerSelectionControlsCatalogDestinationsAndPackingGuide() {
        compose.setContent {
            PackGuideTheme {
                MainScreen(AppSettings(), onThemeChange = {}, onLanguageChange = {}, onLogout = {})
            }
        }
        compose.onNodeWithText("MANO").performClick()
        compose.onNodeWithText("BODEGA").performClick()
        compose.onNodeWithText("CABINA").performClick()
        compose.onNodeWithText("QUITAR MALETA").performClick()
        compose.onNodeWithText("CATÁLOGO").performClick()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: MANO")[0].performClick()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: MANO")[0].performClick()
        compose.onAllNodesWithContentDescription("Aumentar cantidad: BODEGA")[0].performClick()
        compose.onNodeWithText("TOTAL: 3 unidades · 3.6 L").assertExists()
        compose.onNodeWithText("GUÍA").performClick()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
        compose.onNodeWithText("MANO").performClick()
        compose.onNodeWithText("0 de 2 unidades empacadas · 0%").assertExists()
        compose.onNodeWithText("BODEGA").performClick()
        compose.onNodeWithText("0 de 1 unidades empacadas · 0%").assertExists()
        compose.onNodeWithText("RESUMEN").performClick()
        compose.onNodeWithText("TOTAL: 3 unidades · 0 empacadas").assertExists()
        compose.onNodeWithText("CABINA").assertDoesNotExist()
    }
}
