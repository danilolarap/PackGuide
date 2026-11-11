package com.example.packguide

import android.os.Build
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.packguide.settings.AppSettings
import com.example.packguide.ui.screens.MainScreen
import com.example.packguide.ui.theme.PackGuideTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MeasurementsNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()

    private fun launchScreen() {
        compose.runOnUiThread {
            compose.activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (Build.VERSION.SDK_INT >= 27) {
                compose.activity.setShowWhenLocked(true)
                compose.activity.setTurnScreenOn(true)
            }
        }
        compose.setContent { PackGuideTheme { MainScreen(AppSettings(), {}, {}, {}) } }
        compose.waitUntil(10_000) {
            compose.onAllNodesWithText("MEDIDAS DEL EQUIPAJE")
                .fetchSemanticsNodes(atLeastOneRootRequired = false).isNotEmpty()
        }
    }

    private fun confirm(type: String, text: String = "CONFIRMAR MEDIDAS") {
        val tag = "dimension-confirmation-$type"
        compose.onNode(hasScrollAction()).performScrollToNode(hasTestTag(tag))
        compose.onNode(hasText(text) and hasAnyAncestor(hasTestTag(tag))).performClick()
    }

    @Test fun menuAppearsAfterConfirmationAndHidesWhenMeasurementsNeedConfirmationAgain() {
        launchScreen()
        compose.onNodeWithTag("main-navigation").assertDoesNotExist()
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("CONTINUAR AL CATÁLOGO"))
        compose.onNodeWithText("CONTINUAR AL CATÁLOGO").assertIsNotEnabled()
        confirm("Cabin")
        compose.onNodeWithTag("main-navigation").assertIsDisplayed()
        compose.onNodeWithText("CATÁLOGO").performClick()
        compose.onNodeWithText("CATÁLOGO DE ÍTEMS").assertIsDisplayed()
        compose.onNodeWithText("MEDIDAS").performClick()
        confirm("Cabin", "MEDIDAS CONFIRMADAS")
        compose.onNodeWithTag("main-navigation").assertDoesNotExist()
    }

    @Test fun everySelectedBagMustBeConfirmedBeforeTheMenuAppears() {
        launchScreen()
        compose.onNodeWithText("MANO").performClick()
        compose.onNodeWithText("BODEGA").performClick()
        listOf("Personal", "Cabin", "Checked").forEach { type ->
            compose.onNodeWithTag("main-navigation").assertDoesNotExist()
            confirm(type)
        }
        compose.onNodeWithTag("main-navigation").assertIsDisplayed()
        compose.onNodeWithText("GUÍA").performClick()
        compose.onNodeWithText("GUÍA DE EMPACADO").assertIsDisplayed()
    }
}
